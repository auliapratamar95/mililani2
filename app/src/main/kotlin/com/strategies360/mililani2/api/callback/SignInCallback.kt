package com.strategies360.mililani2.api.callback

import android.os.Bundle
import com.google.gson.Gson
import com.strategies360.mililani2.App
import com.strategies360.mililani2.account.AccountConstant
import com.strategies360.mililani2.api.callback.core.CoreCallback
import com.strategies360.mililani2.api.util.OnAPIListener
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.local.auth.SavedAccount
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.mililani2.util.Debugger
import itsmagic.present.simpleaccountmanager.AccountHelper
import retrofit2.Response

/**
 *
 * A sample custom callback for sign in.
 */
class SignInCallback(listener: OnAPIListener<SignInResponse>?) : CoreCallback<SignInResponse>(listener) {

    override fun onSuccess(response: Response<SignInResponse>) {
        // Do heavy job in a different thread,
        // e.g. Store the obtained data list into local table, etc.
        val thread = Thread {
            try {
                addAccount(response.body())
            } catch (e: Exception) {
                Debugger.logException(e)
                callListenerFailure(AppError(response.code(), e.message))
            }
        }

        // Execute the heavy job
        thread.start()
    }

    override fun onFailure(responseCode: Int, errorMessage: String) {
        handleFailure(responseCode, errorMessage)
    }

    @Throws(IllegalStateException::class)
    private fun addAccount(response: SignInResponse?) {
        if (response == null || response.accessToken.isNullOrEmpty() || response.profile == null) {
            throw IllegalStateException("Response is invalid! Consult your backend developer for this problem.")
        }

        val email = response.profile!!.email
        val savedAccount = SavedAccount()
        savedAccount.id = email
        savedAccount.email = email
        savedAccount.accessToken = response.accessToken
        savedAccount.expiryDate = response.expiryDate
        savedAccount.profile = response.profile

        // Create a bundle
        val data = Bundle()
        data.putString(AccountConstant.ACCOUNT_PROFILE, Gson().toJson(savedAccount))

        // Add the account
        AccountHelper(App.context).addAccount(
                savedAccount.email!!,
                savedAccount.accessToken!!,
                data) {
            callListenerSuccess(response)
        }
    }
}
