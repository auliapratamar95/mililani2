package com.strategies360.mililani2.model.remote.caffe.store

import com.google.gson.annotations.SerializedName

class Store {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("address1")
  var address: String? = null

  @SerializedName("city")
  var city: String? = null
}