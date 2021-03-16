package com.strategies360.mililani2.model.remote.caffe.cart

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.remote.caffe.DefaultDetails
import com.strategies360.mililani2.model.remote.caffe.Product

class OrderItems {
  @SerializedName("id")
  var id: String? = null

  @SerializedName("quantity")
  var qty: Int? = null

  @SerializedName("retailPrice")
  var retailPrice: DefaultDetails? = null

  @SerializedName("salePrice")
  var salePrice: DefaultDetails? = null

  @SerializedName("subTotal")
  var subTotal: DefaultDetails? = null

  @SerializedName("orderId")
  var orderId: String? = null

  @SerializedName("skuId")
  var skuId: String? = null

  @SerializedName("product")
  var product: Product? = null

  @SerializedName("status")
  var status: String? = null

  @SerializedName("orderItemAttributes")
  var orderItemAttributes: ArrayList<OrderItemsAttribute>? = null

  @SerializedName("cartModifiers")
  var cartModifiers: ArrayList<CartModifierAttribute>? = null

  @SerializedName("notes")
  var notes: String? = null

  class DiffUtilCallback(
    private val oldList: List<OrderItems> = ArrayList(),
    private val newList: List<OrderItems> = ArrayList()): DiffUtil.Callback() {

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