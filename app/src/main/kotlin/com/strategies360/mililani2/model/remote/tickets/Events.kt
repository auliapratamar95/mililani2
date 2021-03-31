package com.strategies360.mililani2.model.remote.tickets

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Events {

  @SerializedName("ID")
  val id: String? = null

  @SerializedName("ticket_code")
  val ticketCode: String? = null

  @SerializedName("long_description")
  val longDescription: String? = null

  @SerializedName("short_description")
  val shortDescription: String? = null

  @SerializedName("begin_event_date")
  val beginEventDate: String? = null

  @SerializedName("available")
  val available: String? = null

  @SerializedName("end_event_date")
  val endEventDate: String? = null

  @SerializedName("event_time")
  val eventTime: String? = null

  @SerializedName("comments")
  val brochureText: String? = null

  @SerializedName("amount")
  val amount: ArrayList<String>? = null

  class DiffUtilCallback(
    private val oldList: List<Events> = ArrayList(),
    private val newList: List<Events> = ArrayList()): DiffUtil.Callback() {

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