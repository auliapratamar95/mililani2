package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * A Submit Scan MTA Card activity.
 */
class HelpMtaActivity : CoreActivity(), View.OnClickListener {

    override val viewRes = R.layout.activity_help_mta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_toolbar.text = ""

        btn_back.setOnClickListener(this)
        btn_barcode.setOnClickListener(this)
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, HelpMtaActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        if (v == btn_back) {
            onBackPressed()
        } else if (v == btn_barcode) {
            AddScanMtaCardActivity.launchIntent(this)
        }
    }
}
