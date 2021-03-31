package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class PayTicketResponse : AppResponse() {
  @SerializedName("payload")
  val payTicket: PayTicket? = null
}