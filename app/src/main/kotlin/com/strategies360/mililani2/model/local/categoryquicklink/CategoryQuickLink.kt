package com.strategies360.mililani2.model.local.categoryquicklink

import androidx.recyclerview.widget.DiffUtil

class CategoryQuickLink {

    var id: Int? = null
    var name: String? = null

    class DiffUtilCallback(
        private val oldList: List<CategoryQuickLink> = ArrayList(),
        private val newList: List<CategoryQuickLink> = ArrayList()): DiffUtil.Callback() {

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