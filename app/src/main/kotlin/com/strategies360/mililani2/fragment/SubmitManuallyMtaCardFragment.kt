package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.activity.SubmitFinishMtaCardActivity
import com.strategies360.mililani2.activity.SubmitNicknameMtaCardActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.viewmodel.SubmitMTACardViewModel
import kotlinx.android.synthetic.main.fragment_submit_manually_mta_card.*

class SubmitManuallyMtaCardFragment : CoreFragment(), View.OnClickListener {

  private var code: String = ""
  private var isAddMtaCard: Boolean = false

  /** The view model for sign in */
  private val submitMTACardViewModel by lazy {
    ViewModelProviders.of(this)
        .get(SubmitMTACardViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_submit_manually_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    isAddMtaCard =
      (requireActivity().intent.getBooleanExtra(getString(string.prefs_is_add_mta_card), false))

    btn_barcode_mta_card.setOnClickListener(this)
    btn_skip_submit_manual.setOnClickListener(this)

    initViewModel()
  }

  override fun onClick(view: View?) {
    if (view?.id == R.id.btn_barcode_mta_card) {
      if (edit_masked_input.length() == 11) {
        val mtaCardRequest = MTACardRequest()
        code = edit_masked_input.text.toString()
        mtaCardRequest.cardNumber = code

        submitMTACard(mtaCardRequest)
      } else if (edit_masked_input.length() < 11) {
        Common.hideKeyboard(requireActivity())
        Toast
            .makeText(
                requireActivity(),
                "Please enter 8 digit MTA Card number",
                Toast.LENGTH_SHORT
            )
            .show()
      }
      else {
        Common.hideKeyboard(requireActivity())
        Toast
            .makeText(
                requireActivity(),
                "Please type MTA card number",
                Toast.LENGTH_SHORT
            )
            .show()
      }

    }
    else if (view?.id == R.id.btn_skip_submit_manual) {
      BottomMenuNavigationActivity.launchIntent(requireContext())
    }
    else {
      /* nothing to do in here */
    }
  }

  private fun initViewModel() {
    submitMTACardViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onSubmitMTACardLoading()
        Resource.ERROR -> onSubmitMTACardFailure(it.error)
        Resource.SUCCESS -> onSubmitMTACardSuccess(it.data!!)
      }
    })
  }

  /** Load user's profile from a remote server (async)  */
  private fun submitMTACard(data: MTACardRequest) {
    submitMTACardViewModel.submitInMililani(data)
  }

  private fun onSubmitMTACardLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        submitMTACardViewModel.cancelSubmitMTACArd()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onSubmitMTACardSuccess(response: SignInMililaniResponse) {
    Common.dismissProgressDialog()
    if (response.data != null) {
      if (isAddMtaCard) {
        SubmitNicknameMtaCardActivity.launchIntent(requireContext(), code)
      } else {
        SubmitFinishMtaCardActivity.launchIntent(requireContext(), code)
      }
    }
  }

  private fun onSubmitMTACardFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      if (error.code == 1000)
        openDialogNotice()
      else
        Common.showMessageDialog(it, "Error", error.message)
    }
  }

  private fun openDialogNotice() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      DialogNoticeAddCard()
          .show(fragManager, "Dialog")
    }
  }
}