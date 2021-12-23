package com.strategies360.mililani2.model.remote.assessment.content.tab.accordion

import com.google.gson.annotations.SerializedName

class Accordions {

    @SerializedName("title")
    val title: String? = null

    @SerializedName("subtitle")
    val subtitle: String? = null

    @SerializedName("content")
    val contentList: ArrayList<DetailContentAccordions>? = null
}