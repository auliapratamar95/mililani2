package com.strategies360.mililani2.model.remote.product

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class SampleProductListResponse : AppResponse() {

    @SerializedName("data")
    var productList: List<SampleProduct>? = null
}
