package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class DefaultDetails {
  @SerializedName("amount")
  var amount: Double? = null

  @SerializedName("currency")
  var currency: String? = null
}
