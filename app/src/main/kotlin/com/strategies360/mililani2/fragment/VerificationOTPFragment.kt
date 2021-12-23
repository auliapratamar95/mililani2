package com.strategies360.mililani2.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.activity.LoginPhoneNumberActivity
import com.strategies360.mililani2.activity.SubmitScanMtaCardActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInMililaniRequest
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.AuthMililaniViewModel
import kotlinx.android.synthetic.main.activity_login.edit_phone_number
import kotlinx.android.synthetic.main.fragment_verification_otp.btn_back
import kotlinx.android.synthetic.main.fragment_verification_otp.layout_countdown_timer
import kotlinx.android.synthetic.main.fragment_verification_otp.txt_countdown_timer
import kotlinx.android.synthetic.main.fragment_verification_otp.txt_phone_number
import kotlinx.android.synthetic.main.fragment_verification_otp.txt_resend_code
import kotlinx.android.synthetic.main.layout_verification_otp.btn_verify_otp
import kotlinx.android.synthetic.main.layout_verification_otp.edit_otp_view
import kotlinx.android.synthetic.main.layout_verification_otp.privacy_policy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class VerificationOTPFragment : CoreFragment() {
  private val TIME_OUT = 60

  private var f: FragmentActivity? = null

  var job: Deferred<Unit>? = null

  var mCallback: OnVerificationStateChangedCallbacks? = null
  var verificationCode: String = ""
  var phoneNumber: String? = null
  var phoneNumberMask: String? = null
  var otp: String = ""

  /** The view model for sign in */
  private val authViewModel by lazy {
    ViewModelProviders.of(this)
        .get(AuthMililaniViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_verification_otp

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    f = activity
    initViewModel()
    initDataOTP()
    initPrivacyPolice()
  }

  private fun initDataOTP() {
    startFirebaseLogin()
  }

  private fun initView(auth: FirebaseAuth) {
    phoneNumber = (requireActivity().intent.getStringExtra(getString(string.prefs_phone_number)))
    phoneNumberMask = (requireActivity().intent.getStringExtra(getString(string.prefs_phone_number_unmask)))
    txt_phone_number.text = phoneNumber
    getOtpNumber(phoneNumber)

    txt_resend_code.setOnClickListener {
      if (phoneNumber != "") getOtpNumber("0895327005753")
    }

    btn_verify_otp.setOnClickListener {
      otp = edit_otp_view.otp.toString()
      if (otp != "") {
        val credential =
                PhoneAuthProvider.getCredential(verificationCode, otp)
        SigninWithPhone(auth, credential)
      } else {
        Toast
                .makeText(
                        requireContext(),
                        "Please type OTP number",
                        Toast.LENGTH_SHORT
                )
                .show()
      }
    }

    btn_back.setOnClickListener{
      LoginPhoneNumberActivity.launchIntent(requireContext())
    }

  }

  private fun SigninWithPhone(auth: FirebaseAuth, credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            if (job!!.isActive)
              job!!.cancel()
            setPhoneNumber(auth)
          } else {
            Toast.makeText(requireContext(), "Incorrect OTP", Toast.LENGTH_SHORT)
                .show()
          }
        }
  }

  private fun setPhoneNumber(auth: FirebaseAuth) {
    val user = auth.currentUser
    try {
      // User ID token retrival  TODO: not sure what to do with the token yet.
      user!!.getIdToken(true)
          .addOnCompleteListener { task: Task<GetTokenResult> ->
            if (task.isSuccessful) {
              val idToken = task.result?.token

              signIn(idToken)
//              SubmitScanMtaCardActivity.launchIntent(requireContext())
            }
          }
    } catch (e: Exception) {
      Toast.makeText(requireContext(), "Phone number not found", Toast.LENGTH_SHORT).show()
    }
  }

  private fun getOtpNumber(otpNumber: String?) {
    PhoneAuthProvider.getInstance().verifyPhoneNumber(
        otpNumber.toString(),                     // Phone number to verify
        TIME_OUT.toLong(),                           // Timeout duration
        TimeUnit.SECONDS,                // Unit of timeout
        requireActivity(),        // Activity (for callback binding)
        mCallback!!
    )
  }

  private fun startFirebaseLogin() {
    val auth = FirebaseAuth.getInstance()
    mCallback = object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//        Toast.makeText(requireContext(), "verification completed", Toast.LENGTH_SHORT)
//            .show()
        otp = phoneAuthCredential.smsCode.toString()
        edit_otp_view?.setOTP(otp)
        isShowResendCode(true)
      }

      override fun onVerificationFailed(e: FirebaseException) {
        Common.showMessageDialog(requireContext(), "", e.message)
        {
          LoginPhoneNumberActivity.launchIntent(requireContext())
        }
        e.printStackTrace();
      }

      override fun onCodeSent(
        s: String,
        forceResendingToken: ForceResendingToken
      ) {
        super.onCodeSent(s, forceResendingToken)
        verificationCode = s

        isShowResendCode(false)

        if (edit_phone_number.text.isNotEmpty()) {
          phoneNumber = edit_phone_number.text.toString()
        }

        job = if (job == null || job!!.isCancelled)
          countDownAsync()
        else {
          job!!.cancel()
          countDownAsync()
        }
      }
    }

    initView(auth)
  }

  private fun countDownAsync() = GlobalScope.async(Dispatchers.IO) {
    hideKeyboard()

    repeat(TIME_OUT + 1) {
      val res = DecimalFormat("00").format(TIME_OUT - it)
      withContext(Dispatchers.Main) {
        txt_countdown_timer.text = "00:$res"
      }
      delay(1000)
    }
    isShowResendCode(true)
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
          it, wordToSpan,
          arrayOf("Privacy Policy", "https://www.strategies360.com/privacy-policy/"),
          76, 90, 1
      )
    }

    privacy_policy.text = wordToSpan2
    privacy_policy.movementMethod = LinkMovementMethod.getInstance()
    privacy_policy.highlightColor = Color.TRANSPARENT
  }

  private fun hideKeyboard() {
    try {
      val inputManager = activity
          ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      val currentFocusedView = requireActivity().currentFocus
      if (currentFocusedView != null) {
        inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun initViewModel() {
    authViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onAuthLoading()
        Resource.ERROR -> onAuthFailure(it.error)
        Resource.SUCCESS -> onAuthSuccess(it.data!!)
      }
    })
  }

  /** Load user's profile from a remote server (async)  */
  private fun signIn(tokenID: String?) {
    val signInMililaniRequest = SignInMililaniRequest()
    signInMililaniRequest.idToken = tokenID
    signInMililaniRequest.oneSignalUserId = "96dfcb3d-199e-4dff-9f0f-65df16c0c2c4"
    authViewModel.signInMililani(signInMililaniRequest)
  }

  private fun onAuthLoading() {
    activity?.let {
      Common.showProgressDialog(it , onBackPress = {
        authViewModel.cancelSignIn()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onAuthSuccess(response: SignInMililaniResponse) {
    Common.dismissProgressDialog()
    if (response.data != null) {
      Hawk.put((Constant.KEY_TOKEN), response.data!!.accessToken)
      val isNewUser = response.data!!.newUser
      if (isNewUser)
        SubmitScanMtaCardActivity.launchIntent(requireContext())
      else
        BottomMenuNavigationActivity.launchIntent(requireContext())
    }
  }

  private fun onAuthFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      Common.showMessageDialog(it, "Error", error.message)
    }
  }

  private fun isShowResendCode(isVisible: Boolean) {
    if (isVisible) {
      txt_resend_code.visibility = View.VISIBLE
      layout_countdown_timer.visibility = View.GONE
    } else {
      txt_resend_code.visibility = View.GONE
      layout_countdown_timer.visibility = View.VISIBLE
    }
  }
}