package com.strategies360.mililani2.model.remote.reservation.recCenters

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.reservation.Reservation

class RecCenter {
    @SerializedName("ID")
    var id: String? = null

    @SerializedName("post_author")
    var postAuthor: String? = null

    @SerializedName("post_date")
    var postDate: String? = null

    @SerializedName("post_title")
    var postTitle: String? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("acf")
    var acf: Acf? = null

    class DiffUtilCallback(
            private val oldList: List<RecCenter> = ArrayList(),
            private val newList: List<RecCenter> = ArrayList()): DiffUtil.Callback() {

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