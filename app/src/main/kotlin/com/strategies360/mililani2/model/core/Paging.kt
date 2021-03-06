package com.strategies360.mililani2.model.core

import com.google.gson.annotations.SerializedName

class Paging {

    @SerializedName("total")
    var total: Int = 0

    @SerializedName("offset")
    var offset: Int = 0

    @SerializedName("limit")
    var limit: Int = 0
}