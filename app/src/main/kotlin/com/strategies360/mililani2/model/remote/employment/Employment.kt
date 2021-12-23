package com.strategies360.mililani2.model.remote.employment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class Employment: AppResponse() {

  @SerializedName("post_content_mobile")
  val postContentEmployment: PostContentEmployment? = null

}