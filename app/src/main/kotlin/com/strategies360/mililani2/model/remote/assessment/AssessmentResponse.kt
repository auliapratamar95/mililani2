package com.strategies360.mililani2.model.remote.assessment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.employment.PostContentEmployment

class AssessmentResponse : AppResponse() {
    @SerializedName("post_content_mobile")
    val postContentAssessment: PostContentAssesment? = null
}