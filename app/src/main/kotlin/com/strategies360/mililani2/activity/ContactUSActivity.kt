package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_contact_us.*

/**
 * A Submit Scan MTA Card activity.
 */
class ContactUSActivity : CoreActivity(), View.OnClickListener {

    override val viewRes = R.layout.activity_contact_us

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        btn_back.setOnClickListener(this)
        btn_scan_barcode.setOnClickListener(this)
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, ContactUSActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        if (v == btn_back) {
            onBackPressed()
        } else if (v == btn_scan_barcode) {
            AddScanMtaCardActivity.launchIntent(this)
        }
    }
}
