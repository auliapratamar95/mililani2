package com.strategies360.mililani2.model.remote.caffe.cart

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class CartResponse: AppResponse() {
    @SerializedName("payload")
    var cart: Cart? = null
}
