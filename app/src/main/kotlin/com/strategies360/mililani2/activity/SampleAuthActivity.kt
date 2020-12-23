package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 *
 * A sample authentication activity.
 */
class SampleAuthActivity : CoreActivity() {

    override val viewRes = R.layout.activity_sample_auth

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SampleAuthActivity::class.java)
            context.startActivity(intent)
        }
    }
}
