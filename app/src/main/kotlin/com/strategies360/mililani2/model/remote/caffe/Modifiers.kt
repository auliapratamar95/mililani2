package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class Modifiers {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("externalId")
  var externalId: String? = null

  @SerializedName("productId")
  var productId: String? = null

  @SerializedName("isDefault")
  var isDefault: Boolean? = null

  @SerializedName("prices")
  var prices: Prices? =  null
}