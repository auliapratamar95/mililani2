package com.strategies360.mililani2.model.remote.mtaCard

import com.google.gson.annotations.SerializedName

class DeleteMtaCardRequest {

  @SerializedName("ids")
  var idsList: ArrayList<String>? = null
}