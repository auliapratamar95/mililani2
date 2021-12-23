package com.strategies360.mililani2.model.remote.reservation

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class ReservationsResponse : AppResponse() {
    @SerializedName("data")
    var reservationList: ArrayList<Reservation>? = null
}
