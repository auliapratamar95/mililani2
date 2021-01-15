package com.strategies360.mililani2.model.remote.mtaCard

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class MTACard {

    @SerializedName("object")
    var objectData: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("is_default")
    var isDefault: Int? = null

    @SerializedName("card_number")
    var cardNumber: String? = null

    @SerializedName("nickname")
    var nickname: String? = null

    class DiffUtilCallback(
        private val oldList: List<MTACard> = ArrayList(),
        private val newList: List<MTACard> = ArrayList()): DiffUtil.Callback() {

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