package com.strategies360.mililani2.model.remote.caffe.cart

import com.google.gson.annotations.SerializedName

class CartRequest {
  @SerializedName("productId")
  var productId: String? = null

  @SerializedName("quantity")
  var quantity: Int? = null

  @SerializedName("notes")
  var notes: String? = null

  @SerializedName("productOptions")
  var productOptions: Map<String, Any>? = null

  @SerializedName("cartModifiers")
  var cartModifiers: ArrayList<CartModifiers>? = null
}