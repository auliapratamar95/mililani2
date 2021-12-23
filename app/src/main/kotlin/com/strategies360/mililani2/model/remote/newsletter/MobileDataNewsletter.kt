package com.strategies360.mililani2.model.remote.newsletter

import com.google.gson.annotations.SerializedName

class MobileDataNewsletter {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("pdf")
    var objectPdf: ObjectPdf? = null
}