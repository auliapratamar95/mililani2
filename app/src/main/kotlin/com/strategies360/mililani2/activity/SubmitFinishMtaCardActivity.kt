package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitFinishMtaCardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_finish_mta_card

    override fun onBackPressed() {
        super.onBackPressed()
        SubmitScanMtaCardActivity.launchIntent(this)
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context, code: String) {
            val intent = Intent(context, SubmitFinishMtaCardActivity::class.java)
            intent.putExtra(context.getString(R.string.prefs_code_barcode),
                code)
            context.startActivity(intent)
        }
    }
}
