package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName

class SignInMililaniRequest() {

    @SerializedName("id_token")
    var idToken: String? = null

    constructor(idToken: String) : this() {
        this.idToken = idToken
    }
}
