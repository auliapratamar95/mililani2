package com.strategies360.mililani2.model.core

import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("paging")
    var paging: Paging? = null
}
