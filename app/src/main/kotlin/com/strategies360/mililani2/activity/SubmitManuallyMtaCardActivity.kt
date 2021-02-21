package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_submit_manually_mta_card.*

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitManuallyMtaCardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_manually_mta_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context, isAddMtaCard: Boolean) {
            val intent = Intent(context, SubmitManuallyMtaCardActivity::class.java)
            intent.putExtra(context.getString(string.prefs_is_add_mta_card),
                isAddMtaCard)
            context.startActivity(intent)
        }
    }
}
