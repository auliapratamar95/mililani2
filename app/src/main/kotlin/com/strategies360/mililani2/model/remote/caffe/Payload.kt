package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class Payload {
    @SerializedName("data")
    var caffeListResponse: ArrayList<Caffe>? = null

    @SerializedName("totalElement")
    var totalElement: Int? = null

    @SerializedName("totalPage")
    var totalPage: Int? = null

    @SerializedName("currentPage")
    var currentPage: Int? = null
}