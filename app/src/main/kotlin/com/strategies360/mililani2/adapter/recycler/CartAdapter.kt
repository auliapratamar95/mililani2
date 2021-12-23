package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CartAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItems
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_cart.view.*

class CartAdapter : DataListRecyclerViewAdapter<OrderItems, ViewHolder>() {
  var mLayoutManager: LayoutManager? = null

  var onItemCartClick: onItemCartClick? = null
  var onEditItemCartClick: onEditItemCartClick? = null

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_cart))
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
      itemView.txt_title.text = data.product?.fullName.toString()
      itemView.txt_price.text = "$" + data.subTotal?.amount.toString()
      itemView.txt_count_product.text = data.qty.toString() + "x"

      itemView.btn_edit_cart.setOnClickListener {
        onEditItemCartClick?.invoke(adapterPosition, data)
      }

      val adapter = CartDetailAdapter()
      if (Hawk.contains(Constant.IS_FLAG_BUTTON_DELETE)) {
        val isFlagEnable: Boolean = Hawk.get(Constant.IS_FLAG_BUTTON_DELETE)
        if (isFlagEnable) {
          itemView.btn_delete_cart.visibility = View.VISIBLE
          itemView.btn_delete_cart.setOnClickListener {
            onItemCartClick?.invoke(adapterPosition, data)
          }
        } else {
          itemView.btn_delete_cart.visibility = View.GONE
        }
      }
      itemView.recycler_detail_cart.layoutManager =
        LinearLayoutManager(
            itemView.recycler_detail_cart.context, LinearLayoutManager.VERTICAL,
            false
        )

      itemView.recycler_detail_cart.isNestedScrollingEnabled = false
      mLayoutManager = itemView.recycler_detail_cart.layoutManager

      adapter.setDataList(data.orderItemAttributes)
      itemView.recycler_detail_cart.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }
}

typealias onItemCartClick = ((position: Int, data: OrderItems) -> Unit)
typealias onEditItemCartClick = ((position: Int, data: OrderItems) -> Unit)
