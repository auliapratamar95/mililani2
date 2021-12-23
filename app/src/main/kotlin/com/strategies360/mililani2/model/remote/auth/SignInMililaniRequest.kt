package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName

class SignInMililaniRequest() {

    @SerializedName("id_token")
    var idToken: String? = null

    @SerializedName("one_signal_user_id")
    var oneSignalUserId: String? = null

    constructor(idToken: String, oneSignalUserId: String) : this() {
        this.idToken = idToken
        this.oneSignalUserId = oneSignalUserId
    }
}
