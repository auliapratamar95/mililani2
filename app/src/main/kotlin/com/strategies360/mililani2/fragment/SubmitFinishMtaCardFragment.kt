package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_submit_finish_mta_card.*

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
    val codeBarcode = (requireActivity().intent.getStringExtra(getString(string.prefs_code_barcode)))

    txt_number_register_mta.text = codeBarcode

    btn_finish_mta_card.setOnClickListener(this)
    btn_skip_personal_information.setOnClickListener(this)
  }

  override fun onClick(view: View?) {
    when (view?.id) {
      R.id.btn_finish_mta_card -> {
        BottomMenuNavigationActivity.launchIntent(requireContext())
      }
      R.id.btn_skip_personal_information -> {
        BottomMenuNavigationActivity.launchIntent(requireContext())
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }
}