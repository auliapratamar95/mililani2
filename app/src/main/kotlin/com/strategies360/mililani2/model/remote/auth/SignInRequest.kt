package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName

class SignInRequest() {

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    constructor(email: String, password: String) : this() {
        this.email = email
        this.password = password
    }
}
