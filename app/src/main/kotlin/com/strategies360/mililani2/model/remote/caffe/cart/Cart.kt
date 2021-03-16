package com.strategies360.mililani2.model.remote.caffe.cart

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.caffe.DefaultDetails

class Cart {
  @SerializedName("totalTax")
  var totalTax: DefaultDetails? = null

  @SerializedName("subTotal")
  var subTotal: DefaultDetails? = null

  @SerializedName("total")
  var total: DefaultDetails? = null

  @SerializedName("orderItems")
  var orderItems: ArrayList<OrderItems>? = null
}