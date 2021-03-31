package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName

class PayTicket {
  @SerializedName("username")
  val username: String? = null

  @SerializedName("action")
  val action: String? = null

  @SerializedName("admin")
  val admin: String? = null

  @SerializedName("ticketApiUrl")
  val ticketApiUrl: String? = null

  @SerializedName("monetra_req_sequence")
  val monetraReqSequence: String? = null

  @SerializedName("monetra_req_timestamp")
  val monetraReqTimestamp: Int? = null

  @SerializedName("monetra_req_hmacsha256")
  val monetraReqHmacsha256: String? = null
}