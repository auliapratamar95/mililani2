package com.strategies360.mililani2.activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.viewmodel.AuthMililaniViewModel
import kotlinx.android.synthetic.main.activity_login.btn_send_otp
import kotlinx.android.synthetic.main.activity_login.edit_phone_number
import kotlinx.android.synthetic.main.activity_login.layout_send_otp
import kotlinx.android.synthetic.main.activity_login.layout_verify_otp
import kotlinx.android.synthetic.main.layout_verification_otp.btn_verify_otp
import kotlinx.android.synthetic.main.layout_verification_otp.edit_otp_view
import kotlinx.android.synthetic.main.layout_verification_otp.privacy_policy
import kotlinx.android.synthetic.main.layout_verification_otp.txt_otp
import kotlinx.android.synthetic.main.layout_verification_otp.txt_sample
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class LoginActivity : CoreActivity() {
  val TIME_OUT = 60

  private var f: FragmentActivity? = null

  lateinit var auth: FirebaseAuth
  var job: Deferred<Unit>? = null

  var mCallback: OnVerificationStateChangedCallbacks? = null
  var verificationCode: String = ""
  var phoneNumber: String = ""

  /** The view model for sign in */
  private val authViewModel by lazy { ViewModelProviders.of(this).get(AuthMililaniViewModel::class.java) }

  private fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

  private fun Context.hideKeyboard(view: View) =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        ).hideSoftInputFromWindow(view.windowToken, 0)
  
  override val viewRes = R.layout.activity_login

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      f = this
      initSplashScreen()
      btn_verify_otp.setOnClickListener {
        SubmitScanMtaCardActivity.launchIntent(this)
      }
    }
  }

  override fun onBackPressed() {
    if (layout_verify_otp.isVisible) {
      layout_verify_otp.visibility = View.GONE
      layout_send_otp.visibility = View.VISIBLE
    } else {
      showMessageDialog()
    }
  }

  private fun initSplashScreen() {
    startFirebaseLogin()
    btn_send_otp.setOnClickListener {
      //TODO send OTP to the selected phone number
      if (edit_phone_number.text != null && edit_phone_number.text.isNotEmpty()) {
        phoneNumber = edit_phone_number.text.toString()
        getOtpNumber(phoneNumber)
      } else {
        hideKeyboard()
        Toast
            .makeText(
                this@LoginActivity,
                "Please type phone number or pick from list",
                Toast.LENGTH_SHORT
            )
            .show()
      }
    }

    txt_otp.setOnClickListener {
      if (txt_otp.text.toString() == "Resend code") {
        if (phoneNumber != "" && phoneNumber.isNotEmpty()) getOtpNumber(phoneNumber)
      }
    }
  }

  private fun SigninWithPhone(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            if (job!!.isActive)
              job!!.cancel()
            setPhoneNumber()
          } else {
            Toast.makeText(this@LoginActivity, "Incorrect OTP", Toast.LENGTH_SHORT)
                .show()
          }
        }
  }

  private fun setPhoneNumber() {
    val user = auth.currentUser
    try {
      // User ID token retrival  TODO: not sure what to do with the token yet.
      user!!.getIdToken(true)
          .addOnCompleteListener { task: Task<GetTokenResult> ->
            if (task.isSuccessful) {
              val idToken = task.result?.token
              txt_sample.text = idToken
//              Toast.makeText(this, "Token = $idToken", Toast.LENGTH_SHORT).show()
//              txt_phone_number.text = user.phoneNumber
              SubmitScanMtaCardActivity.launchIntent(this)
            }
          }
    } catch (e: Exception) {
      Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
    }
  }

  private fun getOtpNumber(otpNumber: String) {
    PhoneAuthProvider.getInstance().verifyPhoneNumber(
        otpNumber,                     // Phone number to verify
        TIME_OUT.toLong(),                           // Timeout duration
        TimeUnit.SECONDS,                // Unit of timeout
        this@LoginActivity,        // Activity (for callback binding)
        mCallback!!
    )
  }

  private fun startFirebaseLogin() {
    auth = FirebaseAuth.getInstance()
    mCallback = object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        Toast.makeText(this@LoginActivity, "verification completed", Toast.LENGTH_SHORT)
            .show()
        val otp = phoneAuthCredential.smsCode.toString()
        edit_otp_view?.setOTP(otp)

        btn_verify_otp.setOnClickListener {
          if (edit_otp_view.isNotEmpty()) {
            val credential =
              PhoneAuthProvider.getCredential(verificationCode, otp)
            SigninWithPhone(credential)
          } else {
            Toast
                .makeText(
                    this@LoginActivity,
                    "Please type OTP number",
                    Toast.LENGTH_SHORT
                )
                .show()
          }
//          SubmitScanMtaCardActivity.launchIntent(this@LoginActivity)

        }
      }

      override fun onVerificationFailed(e: FirebaseException) {
        Toast.makeText(
            this@LoginActivity, "The format of the phone number provided is incorrect.",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("FirebaseException", e.toString())
      }

      override fun onCodeSent(
        s: String,
        forceResendingToken: ForceResendingToken
      ) {
        super.onCodeSent(s, forceResendingToken)
        verificationCode = s

        initLayoutVerificationOTP()

        if (edit_phone_number.text.isNotEmpty()) {
          phoneNumber = edit_phone_number.text.toString()
//          txt_phone_number.text = phoneNumber
        }

        job = if (job == null || job!!.isCancelled)
          countDownAsync()
        else {
          job!!.cancel()
          countDownAsync()
        }
      }
    }
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

  private fun countDownAsync() = GlobalScope.async(Dispatchers.IO) {
    hideKeyboard()

    repeat(TIME_OUT + 1) {
      val res = DecimalFormat("00").format(TIME_OUT - it)
      println("Kotlin Coroutines World! $res")
      withContext(Dispatchers.Main) {
        txt_otp.text = "00:$res"
      }
      delay(1000)
    }
    txt_otp.text = resources.getString(R.string.resend_code)
  }

  private fun initLayoutVerificationOTP() {
    layout_send_otp.visibility = View.GONE
    layout_verify_otp.visibility = View.VISIBLE

    initPrivacyPolice()
  }

  private fun initPrivacyPolice() {
    val fontType: Typeface? = f?.let { Common.setFont(it, Common.FontType.ROBOTO_REGULAR) }
    privacy_policy.typeface = fontType

    val wordToSpan: Spannable? = f?.let {
      Common.openDialogPrivacyPolicy(
          it, privacy_policy.text.toString(),
          arrayOf("Terms of Use", "https://www.strategies360.com/privacy-policy/"), 58, 71, 0
      )
    }
    val wordToSpan2: Spannable? = f?.let {
      Common.openDialogPrivacyPolicy(
          it, wordToSpan, arrayOf("Privacy Policy", "https://www.strategies360.com/privacy-policy/"),
          76, 90, 1
      )
    }

    privacy_policy.text = wordToSpan2
    privacy_policy.movementMethod = LinkMovementMethod.getInstance()
    privacy_policy.highlightColor = Color.TRANSPARENT
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, LoginActivity::class.java)
      context.startActivity(intent)
    }
  }
}