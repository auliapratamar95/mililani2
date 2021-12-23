package com.strategies360.mililani2.model.remote.notification

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class NotificationResponse : AppResponse() {
    @SerializedName("data")
    var notificationResponse: ArrayList<Notification>? = null
}