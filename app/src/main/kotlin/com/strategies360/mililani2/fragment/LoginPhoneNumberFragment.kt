package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.VerificationOTPActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.Common
import kotlinx.android.synthetic.main.activity_login.btn_send_otp
import kotlinx.android.synthetic.main.fragment_login_phone_number.edit_mask_phone_number

class LoginPhoneNumberFragment : CoreFragment() {

  override val viewRes = R.layout.fragment_login_phone_number

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initLogin()
  }

  private fun initLogin() {
    btn_send_otp.setOnClickListener {
      if (edit_mask_phone_number.text != null && edit_mask_phone_number.text!!.isNotEmpty()) {
        val phoneNumber = "+1" + edit_mask_phone_number.unMaskedText
        val phoneNumberUnMask = "+1" + edit_mask_phone_number.text.toString()
        VerificationOTPActivity.launchIntent(requireContext(), phoneNumber, phoneNumberUnMask)
      } else {
        Common.hideKeyboard(requireActivity())
        Toast
            .makeText(
                requireActivity(),
                "Please type phone number or pick from list",
                Toast.LENGTH_SHORT
            )
            .show()
      }
    }
  }
}