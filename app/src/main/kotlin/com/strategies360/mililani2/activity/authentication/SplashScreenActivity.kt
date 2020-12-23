package com.strategies360.mililani2.activity.authentication

import ScreenSize
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.Transition
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.anim
import com.strategies360.mililani2.R.color
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.LoginActivity
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Config
import com.strategies360.mililani2.view.BackgroundTransitionGenerator
import kotlinx.android.synthetic.main.activity_splash_screen.bg_screen
import kotlinx.android.synthetic.main.activity_splash_screen.btn_send_otp
import kotlinx.android.synthetic.main.activity_splash_screen.edit_otp
import kotlinx.android.synthetic.main.activity_splash_screen.edit_phone_number
import kotlinx.android.synthetic.main.activity_splash_screen.layout_send_otp
import kotlinx.android.synthetic.main.activity_splash_screen.layout_verify_otp
import kotlinx.android.synthetic.main.activity_splash_screen.loading_line_progressbar
import kotlinx.android.synthetic.main.activity_splash_screen.logo_mililani
import kotlinx.android.synthetic.main.activity_splash_screen.main_layout
import kotlinx.android.synthetic.main.layout_verification_otp.btn_verify_otp
import kotlinx.android.synthetic.main.layout_verification_otp.edit_otp_view
import kotlinx.coroutines.Deferred
import java.util.concurrent.TimeUnit

/**
 *
 * Sample splash screen activity.
 * This splash screen already handles its lifecycle, meaning that the
 * pending intent doesn't launch even after user has closed the app.
 */
@RequiresApi(VERSION_CODES.LOLLIPOP)
class SplashScreenActivity : CoreActivity() {

  val TIME_OUT = 60

  private var f: FragmentActivity? = null
  private var screenCalc = 0
  private var fromTop = 0
  private var toBottom = 0

  var job: Deferred<Unit>? = null

  private val mparcel: Map<*, *>? = null

  private var layoutParamsTitleApps: LayoutParams? = null

  private var layoutParamsBottom: RelativeLayout.LayoutParams? = null

  lateinit var auth: FirebaseAuth

  var mCallback: OnVerificationStateChangedCallbacks? = null
  var verificationCode: String = ""

  private fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

  private fun Context.hideKeyboard(view: View) =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        ).hideSoftInputFromWindow(view.windowToken, 0)

  override val viewRes = R.layout.activity_splash_screen

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      f = this
      onSplashScreenReady()
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

  /** Called when app is ready to continue with its splash screen  */
  private fun onSplashScreenReady() {
    // Check for storage space before continuing
    // Low-end devices should have at least 64MB of free space, otherwise 128MB
    val storageThreshold = if (Config.isLowRamDevice()) 64 else 128
    if (Config.freeInternalSpaceInMb < storageThreshold)
      showStorageSpaceWarningDialog()
    else
      initSplashScreen()
  }

  private fun initSplashScreen() {
    initAnimation()
//    startFirebaseLogin()
//    setPhoneNumber()

    btn_send_otp.setOnClickListener {
      //TODO send OTP to the selected phone number
      if (edit_phone_number.text != null && edit_phone_number.text.isNotEmpty()) {
        loading_line_progressbar.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            edit_phone_number.text.toString(),                     // Phone number to verify
            TIME_OUT.toLong(),                           // Timeout duration
            TimeUnit.SECONDS,                // Unit of timeout
            this@SplashScreenActivity,        // Activity (for callback binding)
            mCallback!!
        )
      } else {
        hideKeyboard()
        Toast
            .makeText(
                this@SplashScreenActivity,
                "Please type phone number or pick from list",
                Toast.LENGTH_SHORT
            )
            .show()
      }
    }
  }

  /**
   * Display an [AlertDialog].
   * The dialog contains a warning message about storage space.
   * When the dialog is dismissed, the app will continue.
   */
  private fun showStorageSpaceWarningDialog() {
    let {
      val builder = AlertDialog.Builder(it, R.style.AppTheme_Dialog_Alert)
      builder.setTitle(resources.getString(R.string.info_warning))
      builder.setMessage(resources.getString(R.string.warning_storage_space))
      builder.setPositiveButton(
          resources.getString(android.R.string.ok)
      ) { dialog, _ -> dialog.dismiss() }
      builder.setOnDismissListener { initSplashScreen() }
      builder.show()
    }
  }

  private fun initAnimation() {
    screenCalc = ScreenSize.instance(f!!)!!.height / 14

    val screen_width: Int = ScreenSize.instance(f!!)!!.width
    val screen_height: Int = ScreenSize.instance(f!!)!!.height
    val rasio: String? = Common.ratio(screen_width, screen_height)

    fromTop = screenCalc * 1
    toBottom = screenCalc * 3
    if (rasio.equals("16:9", ignoreCase = true)) toBottom = screenCalc * 2
    layoutParamsTitleApps = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    layoutParamsTitleApps!!.setMargins(0, fromTop, 0, 0) ////l,t,r,b

    layoutParamsTitleApps!!.gravity = Gravity.CENTER_HORIZONTAL
    logo_mililani.layoutParams = layoutParamsTitleApps
    logo_mililani.requestLayout()

    layoutParamsBottom = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParamsBottom!!.setMargins(0, 0, 0, ScreenSize.instance(f!!)!!.height / 14) ////l,t,r,b

    layoutParamsBottom!!.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    layout_send_otp.layoutParams = layoutParamsBottom
    layout_send_otp.requestLayout()

    main_layout.visibility = View.GONE

    val durAnimKenBurnEffect: Int
    durAnimKenBurnEffect = if (null == mparcel) {
      //show title before animate background transmition end
      Handler(Looper.getMainLooper()).postDelayed({
        runOnUiThread(
            Runnable { //smoothAnimateMarginChange(ScreenSize.instance(f).getHeight()/2, PixelCalc.DpToPixel(getResources().getDimension(R.dimen.Register_Margin_TopToLogo), f), 800, 0);
              logo_mililani.visibility = View.VISIBLE
              val animation = AnimationUtils.loadAnimation(f, anim.smooth_fadein)
              logo_mililani.startAnimation(animation)
            })
      }, 1000)
      2000
    } else {
      logo_mililani.visibility = View.VISIBLE
      main_layout.visibility = View.VISIBLE
      smoothAnimateMarginChange(fromTop, toBottom, 0, 0)
      changeColorAnimation(0)
      0
    }

    val na = false

    if (!na) {
      bg_screen.setTransitionListener(onTransittionListener())
      val ACCELERATE_DECELERATE = AccelerateDecelerateInterpolator()
      val generator =
        BackgroundTransitionGenerator(durAnimKenBurnEffect.toLong(), ACCELERATE_DECELERATE)
      bg_screen.setTransitionGenerator(generator)
    }
  }

  private fun onTransittionListener(): KenBurnsView.TransitionListener? {
    return object : KenBurnsView.TransitionListener {
      override fun onTransitionStart(transition: Transition) {
        //Toast.makeText(f, "start", Toast.LENGTH_SHORT).show();
      }

      override fun onTransitionEnd(transition: Transition) {
        bg_screen.pause()
        //Toast.makeText(f, "end", Toast.LENGTH_SHORT).show();
        if (null == mparcel) {
          Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
              main_layout.visibility = View.VISIBLE
              val animation = AnimationUtils.loadAnimation(
                  f, anim.smooth_fadein
              )
              main_layout.startAnimation(animation)
              //mainLayout.setBackgroundColor(getColor(R.color.em_white_80));
              changeColorAnimation(1500)
              Handler(Looper.getMainLooper()).postDelayed({
                runOnUiThread { //smoothAnimateMarginChange(PixelCalc.DpToPixel(50, f), PixelCalc.DpToPixel(getResources().getDimension(R.dimen.Register_Margin_TopToLogo), f), 800, 0);
                  //smoothAnimateMarginChange(PixelCalc.DpToPixel(40, f), PixelCalc.DpToPixel(60, f), 800, 0);
                  smoothAnimateMarginChange(fromTop, toBottom, 800, 0)
                }
              }, 600)
            }
          }, 200)
        }
      }
    }
  }

  private fun smoothAnimateMarginChange(
    fromMargin: Int,
    toMargin: Int,
    duration: Int,
    v: Int
  ) {
    val varl = ValueAnimator.ofInt(fromMargin, toMargin)
    varl.duration = duration.toLong()
    varl.addUpdateListener { animation ->
      when (v) {
        0 -> {
          layoutParamsTitleApps!!.topMargin = (animation.animatedValue as Int)
          //titleApps.setLayoutParams(layoutParamsTitleApps);
          //titleApps.requestLayout();
          logo_mililani.layoutParams = layoutParamsTitleApps
          logo_mililani.requestLayout()
          LoginActivity.launchIntent(this)
          finish()
        }
        1 -> {
        }
      }
    }
    varl.start()
  }

  private fun changeColorAnimation(duration: Int) {
    val colorFrom = ContextCompat.getColor(this, color.Register_Main_Layout)
    val colorTo = ContextCompat.getColor(this, color.em_white_87)

    val colorAnimation = ValueAnimator.ofArgb(colorFrom, colorTo)
    colorAnimation.duration = duration.toLong() // milliseconds
    colorAnimation.addUpdateListener { animator ->
      main_layout.setBackgroundColor(
          animator.animatedValue as Int
      )
    }
    colorAnimation.start()
  }

  private fun SigninWithPhone(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            setPhoneNumber()
          } else {
            Toast.makeText(this@SplashScreenActivity, "Incorrect OTP", Toast.LENGTH_SHORT)
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
//              Toast.makeText(this, "Token = $idToken", Toast.LENGTH_SHORT).show()
//              txt_phone_number.text = user.phoneNumber
            }
          }
    } catch (e: Exception) {
      Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
    }
  }

  private fun startFirebaseLogin() {
    auth = FirebaseAuth.getInstance()
    mCallback = object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        Toast.makeText(this@SplashScreenActivity, "verification completed", Toast.LENGTH_SHORT)
            .show()

        edit_otp_view?.setOTP(phoneAuthCredential.smsCode.toString())

        btn_verify_otp.setOnClickListener {
          if (edit_otp_view.isNotEmpty()) {
            val credential =
              PhoneAuthProvider.getCredential(verificationCode, edit_otp.text.toString())
            SigninWithPhone(credential)
          } else {
            Toast
                .makeText(
                    this@SplashScreenActivity,
                    "Please type OTP number",
                    Toast.LENGTH_SHORT
                )
                .show()
          }
        }
      }

      override fun onVerificationFailed(e: FirebaseException) {
        Toast.makeText(this@SplashScreenActivity, "The format of the phone number provided is incorrect.", Toast.LENGTH_SHORT).show()
        Log.d("FirebaseException", e.toString())
      }

      override fun onCodeSent(
        s: String,
        forceResendingToken: ForceResendingToken
      ) {
        super.onCodeSent(s, forceResendingToken)
        verificationCode = s

        layout_send_otp.visibility = View.GONE
        layout_verify_otp.visibility = View.VISIBLE

        if (edit_phone_number.text.isNotEmpty()) {
          val phoneNumber = edit_phone_number.text.toString()
//          txt_phone_number.text = phoneNumber
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
      finish()
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
}
