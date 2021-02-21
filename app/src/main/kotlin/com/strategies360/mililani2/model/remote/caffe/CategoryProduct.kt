package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class CategoryProduct {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var title: String? = null

    @SerializedName("isActive")
    var isActive: String? = null

    @SerializedName("subcategories")
    var subCategoryPrduct: ArrayList<SubCategoryProduct>? = null
}