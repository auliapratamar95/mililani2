package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitNicknameMtaCardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_nickname_mta_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context, cardNumber: String) {
            val intent = Intent(context, SubmitNicknameMtaCardActivity::class.java)
            intent.putExtra(context.getString(string.prefs_card_number),
                cardNumber)
            context.startActivity(intent)
        }
    }
}
