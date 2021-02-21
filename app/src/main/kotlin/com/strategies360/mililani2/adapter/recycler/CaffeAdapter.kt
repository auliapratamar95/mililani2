package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_caffe.view.*

class CaffeAdapter : DataListRecyclerViewAdapter<Caffe, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_caffe))
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

      itemView.txt_title.text = data.title
      itemView.txt_description.text = data.description
      itemView.img_product.load(Constant.URL_IMAGE + data.imgProduct)
      itemView.txt_price.text = data.price
    }
  }
}