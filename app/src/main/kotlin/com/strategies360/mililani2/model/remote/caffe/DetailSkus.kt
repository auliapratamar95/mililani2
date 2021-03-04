package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class DetailSkus {

  @SerializedName("skusName")
  var skusName: String? = null

  @SerializedName("quantity")
  var quantity: Int? = null

  @SerializedName("price")
  var priceSkus: PriceSkus? = null
}