package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.include_toolbar.btn_back
import kotlinx.android.synthetic.main.include_toolbar.btn_barcode

class SettingMtaFragment: CoreFragment() {

  override val viewRes = R.layout.fragment_setting_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)

    btn_barcode.setOnClickListener {
      openBottomCardList()
    }

    btn_back.setOnClickListener{
      BottomMenuNavigationActivity.launchIntent(requireContext())
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }
}