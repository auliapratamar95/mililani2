package com.strategies360.mililani2.model.remote.mtaCard

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class ClassesListResponse : AppResponse() {

    @SerializedName("data")
    var classListResponse: ArrayList<Classes>? = null
}
