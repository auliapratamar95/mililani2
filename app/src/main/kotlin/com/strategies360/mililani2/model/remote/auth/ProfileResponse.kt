package com.strategies360.mililani2.model.remote.auth

import com.strategies360.mililani2.model.core.AppResponse
import com.google.gson.annotations.SerializedName

class ProfileResponse : AppResponse() {

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("expiry_date")
    var expiryDate: String? = null

    @SerializedName("profile_data")
    var profile: Profile? = null
}
