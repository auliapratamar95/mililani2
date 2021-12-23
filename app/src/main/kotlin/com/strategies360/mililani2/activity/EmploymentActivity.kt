package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import kotlinx.android.synthetic.main.activity_activities.btn_back
import kotlinx.android.synthetic.main.activity_activities.btn_scan_barcode

class EmploymentActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_employess

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
      val intent = Intent(context, EmploymentActivity::class.java)
      context.startActivity(intent)
    }
  }
}