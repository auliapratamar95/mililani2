package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_thankyou_page.*

/**
 * A Submit Scan MTA Card activity.
 */
class ThakyouPageActivity : CoreActivity() {

  override val viewRes = R.layout.activity_thankyou_page

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    btn_caffe.setOnClickListener {
      CaffeActivity.launchIntent(this)
    }
  }

  companion object {

    fun launchIntent(context: Context) {
      val intent = Intent(context, ThakyouPageActivity::class.java)
      context.startActivity(intent)
    }
  }
}
