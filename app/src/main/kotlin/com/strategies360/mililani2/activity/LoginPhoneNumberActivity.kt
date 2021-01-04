package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit OTP Number activity.
 */
class LoginPhoneNumberActivity : CoreActivity() {

    override val viewRes = R.layout.activity_login_phone_number

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
            val intent = Intent(context, LoginPhoneNumberActivity::class.java)
            context.startActivity(intent)
        }
    }
}
