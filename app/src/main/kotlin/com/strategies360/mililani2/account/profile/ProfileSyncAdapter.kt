package com.strategies360.mililani2.account.profile

import android.accounts.Account
import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import com.strategies360.mililani2.account.AccountConstant
import com.strategies360.mililani2.model.local.auth.SavedAccount
import com.strategies360.mililani2.util.Debugger
import com.google.gson.Gson
import itsmagic.present.simpleaccountmanager.AccountHelper
import itsmagic.present.simpleaccountmanager.sync.AccountSyncAdapter
import java.util.concurrent.TimeUnit

/**
 *
 * The sync adapter for user's profile.
 */
class ProfileSyncAdapter(context: Context, autoInitialize: Boolean) : AccountSyncAdapter(context, autoInitialize) {

    override fun onBackgroundSync(account: Account, extras: Bundle, authority: String, provider: ContentProviderClient, syncResult: SyncResult) {
        Debugger.log(tag = javaClass.simpleName, message = "Performing sync for account[${account.name}]")
        try {
            // Get the current account's profile
            val accountHelper = AccountHelper(context)
            val data = accountHelper.accountBundle
            val savedAccount = Gson().fromJson(data?.getString(AccountConstant.ACCOUNT_PROFILE), SavedAccount::class.java)
            if (savedAccount == null) {
                Debugger.log(Log.ERROR, javaClass.simpleName, "Saved account is null, removing account")
                accountHelper.removeAccount()
                return
            }

            try {
                // TODO: 06/15/2017 This is where your actual sync process should be done
                // Simulate a long sync process
                Thread.sleep(3000)

                // Set periodic sync every week
                val weekDuration = TimeUnit.DAYS.toMillis(7)
                ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, weekDuration)

                Debugger.log(Log.DEBUG, javaClass.simpleName, "Account sync finished successfully")
            } catch (e: InterruptedException) {
                Debugger.logException(e)

                // Set periodic sync to a day on failure
                val dayDuration = TimeUnit.DAYS.toMillis(1)
                ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, dayDuration)

                Debugger.log(Log.ERROR, javaClass.simpleName, "Account sync finished with some error(s)")
            }

        } catch (e: Exception) {
            Debugger.logException(e)
        }
    }
}
