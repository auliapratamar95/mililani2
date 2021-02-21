package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.viewmodel.SubmitNicknameMTACardViewModel
import kotlinx.android.synthetic.main.fragment_submit_nickname_mta_card.*

class SubmitNicknameMtaCardFragment : CoreFragment(), View.OnClickListener {

  private var code: String = ""
  private var cardNumber: String? = null

  /** The view model for sign in */
  private val setNicknameMTACardViewModel by lazy {
    ViewModelProviders.of(this)
        .get(SubmitNicknameMTACardViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_submit_nickname_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    cardNumber = (requireActivity().intent.getStringExtra(getString(string.prefs_card_number)))

    txt_card_number.text = cardNumber

    btn_save_mta_card.setOnClickListener(this)
    btn_skip_submit_nickname.setOnClickListener(this)

    initViewModel()
  }

  override fun onClick(view: View?) {
    when (view?.id) {
      R.id.btn_save_mta_card -> {
        if (ed_nickname_mta_card.text != null && ed_nickname_mta_card.text!!.isNotEmpty()) {
          val mtaCardRequest = MTACardRequest()
          mtaCardRequest.nickname = ed_nickname_mta_card.text.toString()
          setNicknameMTACardViewModel.editNicknameMtaCard(cardNumber.toString(), mtaCardRequest)
        } else {
          Common.hideKeyboard(requireActivity())
          Toast
              .makeText(
                  requireActivity(),
                  "Please type your Nickname",
                  Toast.LENGTH_SHORT
              )
              .show()
        }
      }
      R.id.btn_skip_submit_nickname -> {
        val mtaCardRequest = MTACardRequest()
        mtaCardRequest.cardNumber = cardNumber
        mtaCardRequest.nickname = ed_nickname_mta_card.text.toString()
        BottomMenuNavigationActivity.launchIntent(requireContext(), true)
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }

  private fun initViewModel() {
    setNicknameMTACardViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onSetNicknameMTACardLoading()
        Resource.ERROR -> onSetNicknameMTACardFailure()
        Resource.SUCCESS -> onSetNicknameMTACardSuccess()
      }
    })
  }

  private fun onSetNicknameMTACardLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        setNicknameMTACardViewModel.cancelSubmitNicknameMTACArd()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onSetNicknameMTACardSuccess() {
    Common.dismissProgressDialog()
    BottomMenuNavigationActivity.launchIntent(requireContext(), true)
  }

  private fun onSetNicknameMTACardFailure() {
    Common.dismissProgressDialog()
  }
}