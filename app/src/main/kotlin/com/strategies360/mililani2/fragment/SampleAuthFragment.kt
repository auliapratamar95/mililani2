package com.strategies360.mililani2.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.strategies360.mililani2.R
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInRequest
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.ValidationHelper
import com.strategies360.mililani2.viewmodel.SampleAuthViewModel
import com.strategies360.mililani2.viewmodel.SavedAccountViewModel
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_sample_auth.*

/**
 *
 * A sample authentication fragment.
 */
class SampleAuthFragment : CoreFragment(), View.OnClickListener {

    override val viewRes = R.layout.fragment_sample_auth

    /** The view model for account existence */
    private val accountViewModel by lazy { ViewModelProviders.of(this).get(SavedAccountViewModel::class.java) }

    /** The view model for sign in */
    private val authViewModel by lazy { ViewModelProviders.of(this).get(SampleAuthViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        // View bindings
        btn_submit.setOnClickListener(this)
    }

    private fun initViewModel() {
        accountViewModel.existsLiveData.observe(viewLifecycleOwner, Observer {
            if (activity != null && it?.data != null) {
                // If user is already signed in, finish this activity
                Common.showToast(String.format(resources.getString(R.string.error_auth_already_logged_in), it.data!!.name))
                activity!!.finish()
            }
        })
        lifecycle.addObserver(accountViewModel)

        authViewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Resource.LOADING -> onSampleAuthLoading()
                Resource.ERROR -> onSampleAuthFailure(it.error)
                Resource.SUCCESS -> onSampleAuthSuccess(it.data!!)
            }
        })
    }

    override fun onClick(view: View) {
        if (view == btn_submit) {
            if (isDataValid) {
                signIn(SignInRequest(
                        txt_username.text.toString(),
                        txt_password.text.toString()))
            }
        }
    }

    /**
     * Checks if user has input the correct values before calling the sign up API.
     * @return true if all data is valid
     */
    private val isDataValid: Boolean
        get() {
            var isValid = true
            clearEditTextError()

            if (ValidationHelper.isEmpty(txt_username)) {
                txt_username.requestFocus()
                txt_username_layout.error = resources.getString(R.string.error_input_username)
                isValid = false
            }

            if (ValidationHelper.isEmpty(txt_password)) {
                if (isValid) txt_password.requestFocus()
                txt_password_layout.error = resources.getString(R.string.error_input_password)
                isValid = false
            }

            return isValid
        }

    /** Clears all [com.google.android.material.textfield.TextInputLayout] on the activity from the error  */
    private fun clearEditTextError() {
        txt_username_layout.isErrorEnabled = false
        txt_password_layout.isErrorEnabled = false
    }

    /** Load user's profile from a remote server (async)  */
    private fun signIn(request: SignInRequest) {
        authViewModel.signIn(request)
    }

    private fun onSampleAuthLoading() {
        activity?.let {
            Common.showProgressDialog(it , onBackPress = {
                authViewModel.cancelSignIn()
                Common.dismissProgressDialog()
            })
        }
    }

    private fun onSampleAuthSuccess(response: SignInResponse) {
        activity?.let {
            val gson = GsonBuilder().setPrettyPrinting().create()
            Common.dismissProgressDialog()
            Common.showMessageDialog(it, "Success", "Successful sign in:\n" + gson.toJson(response.profile), DialogInterface.OnDismissListener { activity!!.finish() })
        }
    }

    private fun onSampleAuthFailure(error: AppError) {
        activity?.let {
            Common.dismissProgressDialog()
            Common.showMessageDialog(it, "Error", error.message)
        }
    }
}
