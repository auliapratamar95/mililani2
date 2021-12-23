package com.strategies360.mililani2.model.remote.assessment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.assessment.content.tab.accordion.Accordions

class PostContentAssesment {

    @SerializedName("titlecarousel")
    val titleCarousel: TitleCarouselAssessment? = null

    @SerializedName("tabcontent")
    val tabContentAssessment: TabContentAssessment? = null

    @SerializedName("accordions")
    val accordions: Accordions? = null
}