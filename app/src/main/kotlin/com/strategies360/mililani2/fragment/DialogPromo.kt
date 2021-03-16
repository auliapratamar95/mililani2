package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.strategies360.mililani2.R

class DialogPromo : DialogFragment(), View.OnClickListener {

  private var btnApply: Button? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_dialog_promo, container)

    btnApply = rootView.findViewById(R.id.btn_apply)

    btnApply?.setOnClickListener(this)

    return rootView
  }

  override fun onClick(v: View?) {
    when (v) {
      btnApply -> {
        dismiss()
      }
    }
  }
}