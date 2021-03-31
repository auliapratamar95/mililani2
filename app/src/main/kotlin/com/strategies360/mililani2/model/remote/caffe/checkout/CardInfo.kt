package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName

class CardInfo {

  @SerializedName("cardNumber")
  var cardNumber: String? = null

  @SerializedName("cvc")
  var cvc: String? = null

  @SerializedName("expirationDate")
  var expirationDate: String? = null

  @SerializedName("expMonth")
  var expMonth: Int? = null

  @SerializedName("expYear")
  var expYear: Int? = null

  @SerializedName("last4")
  var last4: String? = null

  @SerializedName("name")
  var name: String? = null
}