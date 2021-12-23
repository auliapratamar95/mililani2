package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import kotlinx.android.synthetic.main.activity_rec_center.btn_back
import kotlinx.android.synthetic.main.activity_rec_center.btn_scan_barcode

class RecCenterActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_rec_center

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    btn_back.setOnClickListener {
      onBackPressed()
      finish()
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

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, RecCenterActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }
}