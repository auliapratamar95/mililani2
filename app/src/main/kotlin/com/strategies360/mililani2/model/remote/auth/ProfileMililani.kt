package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName

class ProfileMililani {

    @SerializedName("object")
    var objectData: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("firebase_uid")
    var firebaseUid: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("last_login")
    var lastLogin: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("new_user")
    var newUser: Boolean = true
}