package com.strategies360.mililani2.model.remote.contact

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class Contact: AppResponse(){

    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("post_date")
    var postDate: String? = null

    @SerializedName("post_content")
    var postContent: String? = null

    @SerializedName("post_title")
    var postTitle: String? = null

    @SerializedName("comment_status")
    var commentStatus: String? = null

    @SerializedName("acf")
    var acf: Acf? = null
}