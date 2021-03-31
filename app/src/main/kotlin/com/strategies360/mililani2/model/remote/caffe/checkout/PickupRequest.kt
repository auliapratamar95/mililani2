package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName

class PickupRequest {
  @SerializedName("fullName")
  var fullName: String? = null

  @SerializedName("pickupTime")
  var pickupTime: String? = null

  @SerializedName("pickupDate")
  var pickupDate: String? = null

  @SerializedName("phone")
  var phone: String? = null

  @SerializedName("email")
  var email: String? = null
}