package com.strategies360.mililani2.model.remote.caffe

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Caffe {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var title: String? = null

    @SerializedName("priceRange")
    var price: String? = null

    @SerializedName("imgUrl")
    var imgProduct: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("category_name")
    var categoryName: String? = null

    @SerializedName("category_count")
    var categoryCount: Int? = null

    class DiffUtilCallback(
            private val oldList: List<Caffe> = ArrayList(),
            private val newList: List<Caffe> = ArrayList()): DiffUtil.Callback() {

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