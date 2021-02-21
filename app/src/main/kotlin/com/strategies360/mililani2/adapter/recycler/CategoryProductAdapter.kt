package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategoryProductAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.Caffe
import kotlinx.android.synthetic.main.adapter_category.view.*

class CategoryProductAdapter : DataListRecyclerViewAdapter<Caffe, ViewHolder>() {
  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_category))
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

      itemView.txt_category_name.text = data.categoryName
      itemView.txt_category_count.text = data.categoryCount.toString()
    }
  }
}