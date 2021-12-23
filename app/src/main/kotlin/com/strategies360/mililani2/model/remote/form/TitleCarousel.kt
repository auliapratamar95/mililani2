package com.strategies360.mililani2.model.remote.form

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.contact.Acf

class TitleCarousel {

    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("carousel")
    var carousel: ArrayList<String>? = null
}