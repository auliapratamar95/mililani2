package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeDetail
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_caffe.view.img_product
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_description
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_price
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_title

class CaffeAdapter : DataListRecyclerViewAdapter<ProductCaffeDetail, ViewHolder>() {
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
      val plainText = HtmlCompat.fromHtml(data.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY);

      itemView.txt_title.text = data.title
      itemView.txt_description.text = plainText
      itemView.img_product.load(Constant.URL_IMAGE + data.imgProduct)
      itemView.txt_price.text = data.price
    }
  }
}