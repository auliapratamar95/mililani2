package com.strategies360.mililani2.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.strategies360.mililani2.model.core.Resource

/**
 *
 * The sample presenter for splash screen.
 * This sample shows how multiple fake API calls can be handled by one presenter.
 */
class SampleSplashScreenViewModel : ViewModel(), LifecycleObserver {

  /** Delay time before splash screen begins to load the data */
  @Suppress("PrivatePropertyName")
  private val SPLASH_TIME_MILLISECOND = 2000

  /** The preparation async task */
  private var delayTask: AsyncTask<String?, String?, String?>? = null

  /** The async task */
  private var task1: AsyncTask<String?, String?, String?>? = null

  /** The another async task */
  private var task2: AsyncTask<String?, String?, String?>? = null

  /** The LiveData for readying splash screen */
  val readyLiveData = MutableLiveData<Resource<Boolean>>()

  /** The LiveData for the first data */
  val sample1LiveData = MutableLiveData<Resource<String>>()

  /** The LiveData for the second data */
  val sample2LiveData = MutableLiveData<Resource<String>>()

  /** Determines if ready task is already completed */
  private var isReady = false

  lateinit var auth: FirebaseAuth

  var mCallback: OnVerificationStateChangedCallbacks? = null

  private fun startFirebaseLogin() {
    auth = FirebaseAuth.getInstance()
    mCallback = object : OnVerificationStateChangedCallbacks() {
      override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        sample1LiveData.postValue(Resource.success())
      }

      override fun onVerificationFailed(e: FirebaseException) {
//                sample1LiveData.postValue(Resource.error())
      }

      override fun onCodeSent(
        s: String,
        forceResendingToken: ForceResendingToken
      ) {
      }

    }
  }

}
