package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class SignInMililaniResponse : AppResponse() {

    @SerializedName("data")
    var data: ProfileMililani? = null
}
