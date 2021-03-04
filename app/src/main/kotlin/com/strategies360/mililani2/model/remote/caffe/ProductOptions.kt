package com.strategies360.mililani2.model.remote.caffe

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class ProductOptions {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("name")
  var name: String? = null

  @SerializedName("sortOrder")
  var sortOrder: Double? = null

  var statusOnChoice: Boolean? = false

  @SerializedName("allowedValues")
  var allowedValuesList: ArrayList<AllowedValues>? = null

  class DiffUtilCallback(
    private val oldList: List<ProductOptions> = ArrayList(),
    private val newList: List<ProductOptions> = ArrayList()): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
      return oldList.size
    }

    override fun getNewListSize(): Int {
      return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      // TODO: Use your own implementation, this is just a sample
      return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      // TODO: Use your own implementation, this is just a sample
      val oldItem = oldList[oldItemPosition]
      val newItem = newList[newItemPosition]
      return oldItem == newItem
    }
  }
}