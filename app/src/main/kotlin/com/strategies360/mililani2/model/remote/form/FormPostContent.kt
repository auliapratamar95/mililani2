package com.strategies360.mililani2.model.remote.form

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.assessment.content.tab.accordion.Accordions
import com.strategies360.mililani2.model.remote.contact.Acf

class FormPostContent{

    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("titlecarousel")
    var titlecarousel: TitleCarousel? = null

    @SerializedName("accordions")
    var accordions: Accordions? = null
}