package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName

class BillingAddress {

  @SerializedName("addressLine1")
  var addressLine1: String? = null

  @SerializedName("addressLine2")
  var addressLine2: String? = null

  @SerializedName("differentBilling")
  var differentBilling: Boolean? = false

  @SerializedName("fullName")
  var fullName: String? = null

  @SerializedName("city")
  var city: String? = null

  @SerializedName("phone")
  var phone: String? = null

  @SerializedName("postalCode")
  var postalCode: String? = null
}