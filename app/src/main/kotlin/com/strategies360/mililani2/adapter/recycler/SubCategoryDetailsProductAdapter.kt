package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.SubCategoryDetailsProductAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.AllowedValues
import kotlinx.android.synthetic.main.adapter_sub_category_detail_product.view.*

class SubCategoryDetailsProductAdapter : DataListRecyclerViewAdapter<AllowedValues, ViewHolder>() {

  private var isClickedPositionID: String? = null
  private var isSample: Boolean = false
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  var onSubCategoryDetailProductClick: onSubCategoryDetailProductClick? = null

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_sub_category_detail_product))
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
      if (data.amount != null) {
        itemView.txt_price.text = "$" + data.amount.toString()
      } else {
        itemView.txt_price.text = ""
      }
      itemView.txt_name.text = data.name
      itemView.btn_checked.setOnClickListener {
        tmpPosition = adapterPosition
        if (itemView.img_checked.visibility == View.VISIBLE) {
          itemView.img_checked.visibility = View.GONE
          itemView.img_unchecked.visibility = View.VISIBLE
        } else {
          itemView.img_checked.visibility = View.VISIBLE
          itemView.img_unchecked.visibility = View.GONE

          onSubCategoryDetailProductClick?.invoke(adapterPosition, data)
        }
        isLayoutClickItem = true
        notifyDataSetChanged()
      }

      if (isLayoutClickItem) {
        if (tmpPosition == adapterPosition) {
          isLayoutClickItem = false
          itemView.img_checked.visibility = View.VISIBLE
          itemView.img_unchecked.visibility = View.GONE
        } else {
          itemView.img_checked.visibility = View.GONE
          itemView.img_unchecked.visibility = View.VISIBLE
        }
      } else {
        itemView.img_checked.visibility = View.GONE
        itemView.img_unchecked.visibility = View.VISIBLE
      }
    }
  }
}
typealias onSubCategoryDetailProductClick = ((position: Int, data: AllowedValues) -> Unit)