package com.strategies360.mililani2.model.remote.reservation

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Reservation {

  @SerializedName("id")
  var id: Int? = null

  @SerializedName("facility_short_description")
  var facilityShortDescription: String? = null

  @SerializedName("date")
  var date: String? = null

  @SerializedName("time")
  var time: String? = null

  @SerializedName("location_details")
  var locationDetails: String? = null

  @SerializedName("comments")
  var comments: String? = null

  class DiffUtilCallback(
          private val oldList: List<Reservation> = ArrayList(),
          private val newList: List<Reservation> = ArrayList()): DiffUtil.Callback() {

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