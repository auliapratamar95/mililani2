package com.strategies360.mililani2.model.local.auth

import com.strategies360.mililani2.model.remote.auth.Profile
import com.google.gson.annotations.SerializedName

data class SavedAccount(@SerializedName("id") var id: String? = null) {

    @SerializedName("email")
    var email: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("expired_date")
    var expiryDate: String? = null

    @SerializedName("profile_data")
    var profile: Profile? = null
}