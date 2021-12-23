package com.strategies360.mililani2.model.remote.notification

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.mtaCard.MTACard

class Notification {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("contents")
    var content: String? = null

    @SerializedName("senddate")
    var date: String? = null

    class DiffUtilCallback(
        private val oldList: List<Notification> = ArrayList(),
        private val newList: List<Notification> = ArrayList()): DiffUtil.Callback() {

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