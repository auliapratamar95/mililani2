package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.fragment.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_submit_finish_mta_card.btn_finish_mta_card
import kotlinx.android.synthetic.main.fragment_submit_finish_mta_card.txt_number_register_mta

class SubmitFinishMtaCardFragment : CoreFragment(), View.OnClickListener{

  override val viewRes = R.layout.fragment_submit_finish_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initDefaultView()
  }

  private fun initDefaultView() {
    txt_number_register_mta.text = "QR Code Value"
    btn_finish_mta_card.visibility = View.GONE
  }

  override fun onClick(v: View?) {
    when (view?.id) {
      R.id.btn_finish_mta_card -> {
        initDefaultView()
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }
}