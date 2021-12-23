package com.strategies360.mililani2.model.remote.news

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class NewsResponse : AppResponse() {
//    @SerializedName("data")
    var newsList: ArrayList<News>? = null
}
