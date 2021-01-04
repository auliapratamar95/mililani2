package com.strategies360.mililani2.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity

/**
 * A Submit Scan MTA Card activity.
 */
class SubmitScanMtaCardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_submit_scan_mta_card

    override fun onBackPressed() {
        showMessageDialog()
    }

    private fun showMessageDialog(
        dismissListener: DialogInterface.OnDismissListener? = null
    ) {
        val builder = AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
        builder.setMessage(getString(string.close_apps))
        builder.setPositiveButton(android.R.string.yes)
        { dialog, _ -> dialog.dismiss()
            finishAffinity()
        }
        builder.setNegativeButton(
            android.R.string.no
        ) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
        dialog.show()
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SubmitScanMtaCardActivity::class.java)
            context.startActivity(intent)
        }
    }
}
