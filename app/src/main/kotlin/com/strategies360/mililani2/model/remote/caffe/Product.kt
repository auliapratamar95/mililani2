package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class Product {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("fullName")
  var fullName: String? = null

  @SerializedName("description")
  var description: String? = null

  @SerializedName("primaryMedia")
  var primaryMedia: PrimaryData? = null

  @SerializedName("isFeaturedProduct")
  var isFeaturedProduct: Boolean? = null

  @SerializedName("productOptions")
  var productOptionsList: ArrayList<ProductOptions>? = null

  @SerializedName("modifierGroups")
  var modifierGroupsList: ArrayList<ModifierGroups>? = null

  @SerializedName("skus")
  var detailSkus: DetailSkus? = null
}