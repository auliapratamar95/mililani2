package com.strategies360.mililani2.model.remote.caffe.cart

import com.google.gson.annotations.SerializedName

class OrderItemsAttribute {
  @SerializedName("name")
  var name: String? = null

  @SerializedName("value")
  var value: String? = null
}