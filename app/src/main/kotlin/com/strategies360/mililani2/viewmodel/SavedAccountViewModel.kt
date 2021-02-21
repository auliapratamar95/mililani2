package com.strategies360.mililani2.viewmodel

import android.accounts.Account
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.strategies360.mililani2.App
import com.strategies360.mililani2.account.AccountConstant
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.local.auth.SavedAccount
import com.strategies360.mililani2.util.Debugger
import itsmagic.present.simpleaccountmanager.AccountHelper

class SavedAccountViewModel : ViewModel(), LifecycleObserver {

    /** The account helper */
    private var accountHelper: AccountHelper = AccountHelper(App.context)

    /** The LiveData for account availability */
    val existsLiveData = MutableLiveData<Resource<Account>>()

    /** The LiveData for account data */
    val accountData = MutableLiveData<Resource<SavedAccount>>()

    /** Checks if user is already logged in via Account Manager */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun checkAccount() {
        if (accountHelper.isLoggedIn) {
            Debugger.log(Log.DEBUG, javaClass.simpleName, "User is already logged with Android's account manager")

            val data = accountHelper.accountBundle
            val savedAccount: SavedAccount? = Gson().fromJson(data?.getString(AccountConstant.ACCOUNT_PROFILE), SavedAccount::class.java)
            if (savedAccount != null) {
                Debugger.log(Log.DEBUG, javaClass.simpleName, "Saved account data exists: " + savedAccount.toString())
            } else {
                Debugger.log(Log.DEBUG, javaClass.simpleName, "Saved account data doesn't exist")
            }

            existsLiveData.value = Resource.success(accountHelper.account)
            accountData.value = Resource.success(savedAccount)
        } else {
            Debugger.log(Log.DEBUG, javaClass.simpleName, "User is not logged in")
            existsLiveData.value = Resource.success(null)
            accountData.value = Resource.success(null)
        }
    }
}
