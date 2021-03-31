package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName

class BillingRequest {

  @SerializedName("token")
  var token: String? = null

  @SerializedName("billingAddress")
  var billingAddress: BillingAddress? = null

  @SerializedName("cardInfo")
  var cardInfo: CardInfo? = null

  @SerializedName("Options")
  var options: Options? = null
}