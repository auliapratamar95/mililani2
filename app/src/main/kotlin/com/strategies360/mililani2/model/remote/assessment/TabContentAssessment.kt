package com.strategies360.mililani2.model.remote.assessment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.assessment.content.tab.detail.DetailContentFour
import com.strategies360.mililani2.model.remote.assessment.content.tab.detail.DetailContentOne
import com.strategies360.mililani2.model.remote.assessment.content.tab.detail.DetailContentThree
import com.strategies360.mililani2.model.remote.assessment.content.tab.detail.DetailContentTwo

class TabContentAssessment {
    @SerializedName("content_0")
    val contentOneAssessment: DetailContentOne? = null

    @SerializedName("content_2")
    val contentTwoAssessment: DetailContentTwo? = null

    @SerializedName("content_3")
    val contentThreeAssessment: DetailContentThree? = null

    @SerializedName("content_4")
    val contentFourAssessment: DetailContentFour? = null
}