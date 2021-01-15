package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.LoginPhoneNumberActivity
import com.strategies360.mililani2.util.Constant

class DialogConfirmationLogout : DialogFragment(), View.OnClickListener {

  private var btnLogout: Button? = null
  private var btnCancel: Button? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.layout_dialog_confirmation_logout, container)

    btnLogout = rootView.findViewById(R.id.btn_logout)
    btnCancel = rootView.findViewById(R.id.btn_cancel)

    btnLogout?.setOnClickListener(this)
    btnCancel?.setOnClickListener(this)

    return rootView
  }

  private fun signOut() {
    Hawk.delete(Constant.KEY_TOKEN)
    LoginPhoneNumberActivity.launchIntent(requireContext())
  }

  override fun onClick(v: View?) {
    when (v) {
      btnLogout -> {
        signOut()
      }
      btnCancel -> {
        dismiss()
      }
    }
  }
}