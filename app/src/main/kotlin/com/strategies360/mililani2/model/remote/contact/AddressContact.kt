package com.strategies360.mililani2.model.remote.contact

import com.google.gson.annotations.SerializedName

class AddressContact {

    @SerializedName("address")
    val address: String? = null

    @SerializedName("map_title")
    val mapTitle: String? = null

    @SerializedName("lat")
    val lat: String? = null

    @SerializedName("lng")
    val lng: String? = null

    @SerializedName("phone")
    val phone: String? = null

    @SerializedName("rec_center_hours")
    val recCenterHours: ArrayList<RecCenterHours>? = null
}