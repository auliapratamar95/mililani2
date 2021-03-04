package com.strategies360.mililani2.model.remote.caffe

import com.google.gson.annotations.SerializedName

class ModifierGroups {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("externalId")
  var externalId: Int? = null

  @SerializedName("modifiers")
  var modifiersList: ArrayList<Modifiers>? = null
}