package com.strategies360.mililani2.model.remote.product

import com.strategies360.mililani2.model.core.AppResponse
import com.google.gson.annotations.SerializedName

class SampleProductListResponse : AppResponse() {

    @SerializedName("data")
    var productList: List<SampleProduct>? = null
}
