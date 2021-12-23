package com.strategies360.mililani2.model.remote.form

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.assessment.PostContentAssesment
import com.strategies360.mililani2.model.remote.assessment.content.tab.accordion.Accordions
import com.strategies360.mililani2.model.remote.contact.Acf

class FormResponse: AppResponse(){

    @SerializedName("post_content_mobile")
    val formPostContent: FormPostContent? = null
}