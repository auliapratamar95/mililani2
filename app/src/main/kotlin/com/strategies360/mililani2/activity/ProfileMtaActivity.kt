package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import kotlinx.android.synthetic.main.include_toolbar.btn_back
import kotlinx.android.synthetic.main.include_toolbar.btn_barcode

/**
 * A Submit Scan MTA Card activity.
 */
class ProfileMtaActivity : CoreActivity(), View.OnClickListener  {

    override val viewRes = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_back.setOnClickListener(this)
        btn_barcode.setOnClickListener(this)
    }

    private fun openBottomCardList() {
        val fragManager: FragmentManager = supportFragmentManager
        MTACardBottomListFragment()
            .show(fragManager, "Dialog")
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, ProfileMtaActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        if (v == btn_back) {
            onBackPressed()
        } else if (v == btn_barcode) {
            openBottomCardList()
        }
    }
}
