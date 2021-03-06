package com.strategies360.mililani2.model.remote.auth

import com.google.gson.annotations.SerializedName

class Profile {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("birthdate")
    var birthdate: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("height")
    var height: String? = null

    @SerializedName("weight")
    var weight: String? = null

    @SerializedName("description")
    var description: String? = null
}