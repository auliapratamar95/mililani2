package com.strategies360.mililani2.model.remote.tickets

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class EventsResponse : AppResponse() {
    @SerializedName("data")
    var eventsList: ArrayList<Events>? = null
}
