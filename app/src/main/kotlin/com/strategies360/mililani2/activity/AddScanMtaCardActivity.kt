package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_add_scan_mta_card.*

/**
 * A Submit Scan MTA Card activity.
 */
class AddScanMtaCardActivity : CoreActivity() {

  private var isBottomCardList: Boolean = false

  override val viewRes = R.layout.activity_add_scan_mta_card

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    isBottomCardList = (intent.getBooleanExtra(getString(string.prefs_is_add_bottom_card_list), false))

    btn_back.setOnClickListener {
      if (isBottomCardList) {
        BottomMenuNavigationActivity.launchIntent(this)
      } else {
        onBackPressed()
      }
    }
  }

  override fun onBackPressed() {
    if (isBottomCardList) {
      BottomMenuNavigationActivity.launchIntent(this)
    } else {
      super.onBackPressed()
    }
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, AddScanMtaCardActivity::class.java)
      context.startActivity(intent)
    }

    fun launchIntent(context: Context, isBottomMtaCard: Boolean) {
      val intent = Intent(context, AddScanMtaCardActivity::class.java)
      intent.putExtra(context.getString(string.prefs_is_add_bottom_card_list),
          isBottomMtaCard)
      context.startActivity(intent)
    }
  }
}
