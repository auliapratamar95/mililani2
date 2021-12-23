package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.layout_dialog_quick_link.*

class DialogConfirmationQuickLink : DialogFragment(), View.OnClickListener {

  private var btnDismiss: Button? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.layout_dialog_quick_link, container)

    btnDismiss = rootView.findViewById(R.id.btn_dismiss)

    btnDismiss?.setOnClickListener(this)

    return rootView
  }

  override fun onClick(v: View?) {
    when (v) {
      btnDismiss -> {
        if (cb_quick_link.isChecked) {
          Hawk.put((Constant.FLAG_ON_DIALOG), true)
        }
        dismiss()
      }
    }
  }
}