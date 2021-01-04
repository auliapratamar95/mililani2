package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitScanMtaCardFullActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_scan_mta_card

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SubmitScanMtaCardFullActivity::class.java)
            context.startActivity(intent)
        }
    }
}
