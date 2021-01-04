package com.strategies360.mililani2.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.color
import com.strategies360.mililani2.util.Common.FontType.DROID_SANS
import com.strategies360.mililani2.util.Common.FontType.GIBSON_BOLD
import com.strategies360.mililani2.util.Common.FontType.GIBSON_REGULAR
import com.strategies360.mililani2.util.Common.FontType.ROBOTO_BOLD
import com.strategies360.mililani2.util.Common.FontType.ROBOTO_REGULAR
import com.strategies360.mililani2.util.Common.FontType.SOURCE_SANS_PRO_BOLD
import com.strategies360.mililani2.util.Common.FontType.SOURCE_SANS_PRO_LIGHT
import com.strategies360.mililani2.util.Common.FontType.SOURCE_SANS_PRO_REGULAR
import com.strategies360.mililani2.util.Common.FontType.SOURCE_SANS_PRO_SEMIBOLD
import com.strategies360.mililani2.view.OnProgressBackPressed
import com.strategies360.mililani2.view.ProgressDialog

/**
 *
 * A class that handles basic universal methods.
 */
object Common {

    /** The loading progress dialog object */
    @SuppressLint("StaticFieldLeak")
    var progressDialog: ProgressDialog? = null

    /**
     * Shows a loading progress dialog.
     * @param context the context
     * @param stringRes the dialog message string resource id
     * @param onBackPress the back button press listener when loading is shown
     */
    fun showProgressDialog(
        context: Context,
        stringRes: Int = -1,
        onBackPress: OnProgressBackPressed? = null
    ) {
        dismissProgressDialog()
        progressDialog = ProgressDialog(context)
        progressDialog!!.setText(stringRes)
        progressDialog!!.onProgressBackPressed = onBackPress
        if (context is Activity && !context.isFinishing) progressDialog!!.show()
    }

    /** Hides the currently shown loading progress dialog */
    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    /**
     * Sets the progress dialog progress in percent.
     * @param progress The loading progress in percent
     */
    fun setProgressDialogProgress(progress: Int) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setProgress(progress)
            progressDialog!!.setText(progress.toString() + "%")
        }
    }

    /**
     * Sets the progress dialog progress indeterminate state.
     * @param isIndeterminate Determines if progress dialog is indeterminate
     */
    fun setProgressDialogIndeterminate(isIndeterminate: Boolean) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setIndeterminate(isIndeterminate)
        }
    }

    /**
     * Sets the progress dialog message.
     * @param message The dialog message string
     */
    fun setProgressDialogText(message: String) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setText(message)
        }
    }

    /**
     * Display a simple [Toast].
     * @param stringRes The message string resource id
     */
    fun showToast(stringRes: Int) {
        showToast(App.context.getString(stringRes))
    }

    /**
     * Display a simple [Toast].
     * @param message The message string
     */
    fun showToast(message: String) {
        Toast.makeText(App.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Display a simple [AlertDialog] with a simple OK button.
     * If the dismiss listener is specified, the dialog becomes uncancellable
     * @param context The context
     * @param title The title string
     * @param message The message string
     * @param dismissListener The dismiss listener
     */
    fun showMessageDialog(
        context: Context,
        title: String?,
        message: String?,
        dismissListener: DialogInterface.OnDismissListener? = null
    ) {
        val builder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
        dialog.show()
    }

    fun ratio(
        a: Int,
        b: Int
    ): String? {
        val gcd: Int = gcd(a, b)
        return if (a > b) {
            showRatio(a / gcd, b / gcd)
        } else {
            showRatio(b / gcd, a / gcd)
        }
    }

    fun gcd(
        p: Int,
        q: Int
    ): Int {
        return if (q == 0) p else gcd(q, p % q)
    }

    fun showRatio(
        a: Int,
        b: Int
    ): String? {
        val res = "$a:$b"
        return res
    }

    enum class FontType {
        GIBSON_REGULAR,
        GIBSON_BOLD,
        ROBOTO_REGULAR,
        ROBOTO_BOLD,
        DROID_SANS,
        SOURCE_SANS_PRO_BOLD,
        SOURCE_SANS_PRO_SEMIBOLD,
        SOURCE_SANS_PRO_LIGHT,
        SOURCE_SANS_PRO_REGULAR
    }

    fun setFont(
        f: FragmentActivity,
        fontType: FontType?
    ): Typeface? {
        var font: Typeface? = null
        when (fontType) {
            GIBSON_REGULAR -> font =
                Typeface.createFromAsset(f.assets, "fonts/Gibson-Regular.ttf")
            GIBSON_BOLD -> font = Typeface.createFromAsset(f.assets, "fonts/gibson-bold.ttf")
            ROBOTO_REGULAR -> font =
                Typeface.createFromAsset(f.assets, "fonts/Roboto-Regular.ttf")
            ROBOTO_BOLD -> font = Typeface.createFromAsset(f.assets, "fonts/Roboto-Bold.ttf")
            DROID_SANS -> font = Typeface.createFromAsset(f.assets, "fonts/DroidSans.ttf")
            SOURCE_SANS_PRO_BOLD -> font =
                Typeface.createFromAsset(f.assets, "fonts/SourceSansPro-Bold.otf")
            SOURCE_SANS_PRO_SEMIBOLD -> font =
                Typeface.createFromAsset(f.assets, "fonts/SourceSansPro-Semibold.otf")
            SOURCE_SANS_PRO_LIGHT -> font =
                Typeface.createFromAsset(f.assets, "fonts/SourceSansPro-Light.otf")
            SOURCE_SANS_PRO_REGULAR -> font =
                Typeface.createFromAsset(f.assets, "fonts/SourceSansPro-Regular.otf")
        }
        return font
    }

    fun openDialogPrivacyPolicy(
        f: FragmentActivity,
        text: Any?,
        textSpan: Array<String?>?,
        startChar: Int,
        endChar: Int,
        reqcode: Int
    ): Spannable? {
        val wordtoSpan: Spannable = SpannableString(text as CharSequence?)
        wordtoSpan.setSpan(
            object : ClickableSpan() {
                override fun onClick(v: View) {
                    if (reqcode == 0) {

                    } else if (reqcode == 1) {

                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = f.getColor(color.edit_text_normal_login)
                    ds.isUnderlineText = true
                }
            }, startChar, endChar, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return wordtoSpan
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
