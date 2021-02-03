package com.strategies360.mililani2.model.remote.mtaCard

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Classes {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("day")
    var day: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("elgibility")
    var elgibility: String? = null

    @SerializedName("fee")
    var fee: String? = null

    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("meeting_details")
    var meetingDetails: String? = null

    @SerializedName("location")
    var location: String? = null

    @SerializedName("isCalender")
    var isCalender: Boolean? = null

    @SerializedName("max_count")
    var maxCount: String? = null

    @SerializedName("total_count")
    var totalCount: String? = null

    class DiffUtilCallback(
        private val oldList: List<Classes> = ArrayList(),
        private val newList: List<Classes> = ArrayList()): DiffUtil.Callback() {

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