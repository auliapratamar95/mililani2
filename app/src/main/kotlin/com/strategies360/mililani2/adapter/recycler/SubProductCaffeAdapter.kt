package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.activity.CategoryProductDetailActivity
import com.strategies360.mililani2.adapter.recycler.SubProductCaffeAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.SubProductCaffe
import kotlinx.android.synthetic.main.adapter_sub_product_caffe.view.recycler_sub_product_caffe
import kotlinx.android.synthetic.main.adapter_sub_product_caffe.view.txt_title

class SubProductCaffeAdapter : DataListRecyclerViewAdapter<SubProductCaffe, ViewHolder>() {
  private var adapter = SubProductDetailAdapter()

  var mLayoutManager: LayoutManager? = null

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_sub_product_caffe))
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

      itemView.txt_title.text = data.name
      adapter.setDataList(data.productCaffeDetailList)
      adapter.onCategorySubProductClick = {
        CategoryProductDetailActivity.launchIntent(App.context)
      }
      itemView.recycler_sub_product_caffe.adapter = adapter
      itemView.recycler_sub_product_caffe.layoutManager =
        LinearLayoutManager(
            itemView.recycler_sub_product_caffe.context, LinearLayoutManager.VERTICAL, false
        )
      itemView.recycler_sub_product_caffe.isNestedScrollingEnabled = false

      mLayoutManager = itemView.recycler_sub_product_caffe.layoutManager
      adapter.notifyItemChanged(adapterPosition)

    }
  }
}