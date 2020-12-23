package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.fragment.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_submit_manually_mta_card.btn_barcode_mta_card

class SubmitManuallyMtaCardFragment : CoreFragment(), View.OnClickListener{

  override val viewRes = R.layout.fragment_submit_manually_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initDefaultView()
  }

  private fun initDefaultView() {
    btn_barcode_mta_card.visibility = View.GONE
  }

  override fun onClick(v: View?) {
    when (view?.id) {
      R.id.btn_barcode_mta_card -> {
        initDefaultView()
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }
}