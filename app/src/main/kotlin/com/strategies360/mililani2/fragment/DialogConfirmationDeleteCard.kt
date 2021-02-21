package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.eventbus.EventDeleteCardNumber
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.DeleteMtaCardRequest
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.DeleteMTACardViewModel
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.*
import org.greenrobot.eventbus.EventBus

class DialogConfirmationDeleteCard : DialogFragment(), View.OnClickListener {

  private val viewModelDeleteMTACard by lazy {
    ViewModelProviders.of(this)
        .get(DeleteMTACardViewModel::class.java)
  }

  private var btnDeleteCard: Button? = null
  private var btnCancel: Button? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_dialog_confirmation_delete, container)

    btnDeleteCard = rootView.findViewById(R.id.btn_delete_card)
    btnCancel = rootView.findViewById(R.id.btn_cancel)

    initViewModel()
    btnDeleteCard?.setOnClickListener(this)
    btnCancel?.setOnClickListener(this)

    return rootView
  }

  private fun initViewModel() {
    viewModelDeleteMTACard.deleteLiveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onDeleteMTACardLoading()
        Resource.SUCCESS -> onDeleteMTACardSuccess()
        Resource.ERROR -> onDeleteMTACardFailure(it.error)
      }
    })
  }

  private fun onDeleteMTACardLoading() {
  }

  private fun onDeleteMTACardSuccess() {
    EventBus.getDefault().postSticky(EventDeleteCardNumber(true))
    dismiss()
  }

  private fun onDeleteMTACardFailure(error: AppError) {
    progress_bar.visibility = View.GONE
  }

  override fun onClick(v: View?) {
    when (v) {
      btnDeleteCard -> {
        if (Hawk.contains(Constant.KEY_REQUEST_DELETE_CARD_NUMBER)) {
          val deleteMtaCardRequest: DeleteMtaCardRequest = Hawk.get(Constant.KEY_REQUEST_DELETE_CARD_NUMBER)
          viewModelDeleteMTACard.deleteMTACard(deleteMtaCardRequest)
        }
      } btnCancel -> {
        dismiss()
      }
    }
  }
}