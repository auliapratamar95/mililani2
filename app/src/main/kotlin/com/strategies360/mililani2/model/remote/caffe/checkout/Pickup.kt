package com.strategies360.mililani2.model.remote.caffe.checkout

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.caffe.PriceSkus

class Pickup {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("orderNumber")
  var orderNumber: String? = null

  @SerializedName("email")
  var email: String? = null

  @SerializedName("status")
  var status: String? = null

  @SerializedName("totalTax")
  var totalTax: PriceSkus? = null

  @SerializedName("totalIncludedTax")
  var totalIncludedTax: PriceSkus? = null

  @SerializedName("totalShipping")
  var totalShipping: PriceSkus? = null

  @SerializedName("subTotal")
  var subTotal: PriceSkus? = null

  @SerializedName("total")
  var total: PriceSkus? = null

  @SerializedName("dateSubmitted")
  var dateSubmitted: String? = null

  @SerializedName("zoneId")
  var zoneId: String? = null

//  @SerializedName("customer")
//  var customer: String? = null
}