package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 *
 * Sample activity to open another sample activities.
 */
class SampleSelectionActivity : CoreActivity() {

    override val viewRes = R.layout.activity_sample_selection

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SampleSelectionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
