package com.strategies360.mililani2.model.remote.contact

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class ContactUsResponse: AppResponse() {
    @SerializedName("data")
    var caffeListResponse: Contact? = null
}
