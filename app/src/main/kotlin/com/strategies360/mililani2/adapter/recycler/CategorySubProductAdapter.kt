package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategorySubProductAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.SubProductCaffe
import kotlinx.android.synthetic.main.adapter_sub_category.view.txt_category_count
import kotlinx.android.synthetic.main.adapter_sub_category.view.txt_category_name

class CategorySubProductAdapter : DataListRecyclerViewAdapter<SubProductCaffe, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_sub_category))
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
      val count: String = (data.productCaffeDetailList?.size?.plus(1)).toString()

      itemView.txt_category_name.text = data.name
      itemView.txt_category_count.text = count
    }
  }
}