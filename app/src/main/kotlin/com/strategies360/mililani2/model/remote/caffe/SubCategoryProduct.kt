package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class SubCategoryProduct {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("fullName")
    var fullName: String? = null

    @SerializedName("isActive")
    var isActive: String? = null
}