package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CartDetailAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItemsAttribute
import kotlinx.android.synthetic.main.adapter_detail_cart.view.key_required

class CartDetailAdapter : DataListRecyclerViewAdapter<OrderItemsAttribute, ViewHolder>() {
  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_detail_cart))
  }

  override fun onBindDataViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bindView()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bindView() {
      val data = getDataList()[adapterPosition]
      itemView.key_required.text = data.name.toString() + ": " + data.value.toString()
    }
  }
}