package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit OTP Number activity.
 */
class VerificationOTPActivity : CoreActivity() {

    override val viewRes = R.layout.activity_verification_otp

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context, phoneNumber: String, phoneNumberUnMask: String) {
            val intent = Intent(context, VerificationOTPActivity::class.java)
            intent.putExtra(context.getString(R.string.prefs_phone_number),
                phoneNumber)
            intent.putExtra(context.getString(R.string.prefs_phone_number_unmask),
                phoneNumberUnMask)
            context.startActivity(intent)
        }
    }
}
