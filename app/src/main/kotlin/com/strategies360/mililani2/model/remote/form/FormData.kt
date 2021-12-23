package com.strategies360.mililani2.model.remote.form

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.contact.Acf

class FormData {

    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("post_date")
    var postDate: String? = null

    @SerializedName("field_5c259ee976923")
    var titlePage: String? = null

    var titleDetailPage: String? = null

    @SerializedName("acf")
    var acf: Acf? = null
}