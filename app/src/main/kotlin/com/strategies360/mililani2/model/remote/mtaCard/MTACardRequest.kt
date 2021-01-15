package com.strategies360.mililani2.model.remote.mtaCard

import com.google.gson.annotations.SerializedName

class MTACardRequest {

  @SerializedName("card_number")
  var cardNumber: String? = null

  @SerializedName("nickname")
  var nickname: String? = null
}