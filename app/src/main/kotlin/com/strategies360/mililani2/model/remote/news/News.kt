package com.strategies360.mililani2.model.remote.news

import com.google.gson.annotations.SerializedName

class News {
  @SerializedName ("object")
  val objectNews: String? = null

  @SerializedName ("post_title")
  val postTitle: String? = null

  @SerializedName ("post_date")
  val postDate: String? = null

  @SerializedName ("post_content")
  val postContent: String? = null
  var isExpandData: Boolean? = false
}