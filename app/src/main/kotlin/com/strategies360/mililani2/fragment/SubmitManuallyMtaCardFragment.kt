package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.BottomMenuNavigatonActivity
import com.strategies360.mililani2.activity.SubmitScanMtaCardActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_submit_manually_mta_card.btn_barcode_mta_card
import kotlinx.android.synthetic.main.fragment_submit_manually_mta_card.btn_skip_submit_manual

class SubmitManuallyMtaCardFragment : CoreFragment(), View.OnClickListener{

  override val viewRes = R.layout.fragment_submit_manually_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    btn_barcode_mta_card.setOnClickListener(this)
    btn_skip_submit_manual.setOnClickListener(this)
  }

  override fun onClick(view: View?) {
    when (view?.id) {
      R.id.btn_barcode_mta_card -> {
        SubmitScanMtaCardActivity.launchIntent(requireContext())
      }
      R.id.btn_skip_submit_manual -> {
        BottomMenuNavigatonActivity.launchIntent(requireContext())
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }
}