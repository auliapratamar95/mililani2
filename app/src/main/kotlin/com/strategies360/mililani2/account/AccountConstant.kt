package com.strategies360.mililani2.account

import com.strategies360.mililani2.App
import com.strategies360.mililani2.R

/**
 *
 * A class that holds constant variables for app's account manager.
 */
object AccountConstant {

    /** The account type  */
    val ACCOUNT_TYPE: String = App.context.getString(R.string.account_manager_account_type)

    /** The account sync provider  */
    val ACCOUNT_PROVIDER_PROFILE: String = App.context.getString(R.string.account_manager_account_provider_profile)

    /** The bundle key for user's profile  */
    const val ACCOUNT_PROFILE = "account_profile"

}
