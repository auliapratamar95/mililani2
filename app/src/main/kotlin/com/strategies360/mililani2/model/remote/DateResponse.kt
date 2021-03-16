package com.strategies360.mililani2.model.remote

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class DateResponse : AppResponse() {
  @SerializedName("payload")
  val dateResponse: ArrayList<String>? = null
}