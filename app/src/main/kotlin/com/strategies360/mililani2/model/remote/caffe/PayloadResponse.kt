package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class PayloadResponse: AppResponse() {
    @SerializedName("payload")
    var payload: Payload? = null
}