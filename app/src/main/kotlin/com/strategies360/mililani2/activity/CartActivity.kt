package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import kotlinx.android.synthetic.main.activity_caffe.btn_back
import kotlinx.android.synthetic.main.activity_caffe.btn_scan_barcode

class CartActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_cart

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    btn_back.setOnClickListener {
      onBackPressed()
    }

    btn_scan_barcode.setOnClickListener {
      openBottomCardList()
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager = supportFragmentManager
    MTACardBottomListFragment()
        .show(fragManager, "Dialog")
  }

  override fun onBackPressed() {
    super.onBackPressed()
    CaffeActivity.launchIntent(this)
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, CartActivity::class.java)
      context.startActivity(intent)
    }
  }
}