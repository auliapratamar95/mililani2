package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitScanMtaCardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_scan_mta_card

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SubmitScanMtaCardActivity::class.java)
            context.startActivity(intent)
        }
    }
}
