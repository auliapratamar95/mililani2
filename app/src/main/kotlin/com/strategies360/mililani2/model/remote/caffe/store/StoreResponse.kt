package com.strategies360.mililani2.model.remote.caffe.store

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class StoreResponse : AppResponse() {
  @SerializedName("payload")
  var storeResponse: ArrayList<Store>? = null
}
