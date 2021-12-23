package com.strategies360.mililani2.model.remote.newsletter

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.notification.Notification

class Newsletter {
    @SerializedName("ID")
    var id: Int? = null

    @SerializedName("post_title")
    var postName: String? = null

    @SerializedName("mobiledata")
    var mobileData: MobileDataNewsletter? = null

    class DiffUtilCallback(
        private val oldList: List<Newsletter> = ArrayList(),
        private val newList: List<Newsletter> = ArrayList()): DiffUtil.Callback() {

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