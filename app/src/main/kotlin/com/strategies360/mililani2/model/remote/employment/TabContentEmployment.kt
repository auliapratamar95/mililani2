package com.strategies360.mililani2.model.remote.employment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.employment.contentemployment.ContentThreeEmployment

class TabContentEmployment {
    @SerializedName("content_2")
    val contentTwoEmployment: ContentTwoEmployment? = null

    @SerializedName("content_3")
    val contentThreeEmployment: ContentThreeEmployment? = null
}