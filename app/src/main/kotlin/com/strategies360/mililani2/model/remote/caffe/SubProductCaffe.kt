package com.strategies360.mililani2.model.remote.caffe

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class SubProductCaffe {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("fullName")
    var fullName: String? = null

    @SerializedName("isActive")
    var isActive: String? = null

    @SerializedName("products")
    var productCaffeDetailList: ArrayList<ProductCaffeDetail>? = null

    class DiffUtilCallback(
        private val oldList: List<SubProductCaffe> = ArrayList(),
        private val newList: List<SubProductCaffe> = ArrayList()): DiffUtil.Callback() {

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