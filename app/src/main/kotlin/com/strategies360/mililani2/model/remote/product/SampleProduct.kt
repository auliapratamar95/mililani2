package com.strategies360.mililani2.model.remote.product

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class SampleProduct(@SerializedName("id") var id: Int = 0) {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("image_thumb")
    var imageThumbnail: String? = null

    override fun toString(): String {
        return "[$id]: $name"
    }

    class DiffUtilCallback(
            private val oldList: List<SampleProduct> = ArrayList(),
            private val newList: List<SampleProduct> = ArrayList()): DiffUtil.Callback() {

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
