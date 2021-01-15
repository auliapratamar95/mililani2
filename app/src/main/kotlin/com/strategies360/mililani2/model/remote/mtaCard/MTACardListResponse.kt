package com.strategies360.mililani2.model.remote.mtaCard

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class MTACardListResponse : AppResponse() {

    @SerializedName("data")
    var mtaCarfList: ArrayList<MTACard>? = null
}
