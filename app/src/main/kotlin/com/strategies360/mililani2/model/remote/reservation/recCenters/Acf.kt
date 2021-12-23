package com.strategies360.mililani2.model.remote.reservation.recCenters

import com.google.gson.annotations.SerializedName

class Acf {

    @SerializedName("rec_center_description")
    val description: String? = null

    @SerializedName("address")
    val address: Address? = null

    @SerializedName("rec_center_hours")
    val recCenterSchedule: ArrayList<DetailReccenterHour>? = null
}