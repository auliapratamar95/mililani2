package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.eventbus.EventPrimaryCardNumber
import com.strategies360.mililani2.eventbus.EventPromoCode
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_custom_card_list.view.*
import org.greenrobot.eventbus.EventBus

class DialogPromo : DialogFragment(), View.OnClickListener {

  private var btnApply: Button? = null
  private var edPromo: EditText? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_dialog_promo, container)

    btnApply = rootView.findViewById(R.id.btn_apply)
    edPromo = rootView.findViewById(R.id.ed_promo)

    btnApply?.setOnClickListener(this)

    return rootView
  }

  override fun onClick(v: View?) {
    when (v) {
      btnApply -> {
        if (edPromo?.text.toString() != "") {
          EventBus.getDefault().postSticky(
            EventPromoCode(
              edPromo?.text.toString()
            )
          )
        }
        dismiss()
      }
    }
  }
}