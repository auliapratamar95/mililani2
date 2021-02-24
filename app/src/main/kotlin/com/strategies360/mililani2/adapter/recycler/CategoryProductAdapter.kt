package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategoryProductAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.ProductCaffe
import kotlinx.android.synthetic.main.adapter_category.view.btn_category_product
import kotlinx.android.synthetic.main.adapter_category.view.recycler_sub_product_caffe
import kotlinx.android.synthetic.main.adapter_category.view.txt_category_count
import kotlinx.android.synthetic.main.adapter_category.view.txt_category_name

class CategoryProductAdapter : DataListRecyclerViewAdapter<ProductCaffe, ViewHolder>() {
  private var adapter = CategorySubProductAdapter()
  var mLayoutManager: LayoutManager? = null

  var onItemClick: onItemClick? = null

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
      val count: String = (data.productCaffeDetailList?.size.toString())
      if (count == "null") {
        var countSubProduct = 0
        itemView.txt_category_name.text = data.title
        itemView.recycler_sub_product_caffe.visibility = View.VISIBLE

        for (i in data.subProductCaffeList!!.indices) {
          countSubProduct += data.subProductCaffeList!![i].productCaffeDetailList?.size!!
        }

        itemView.txt_category_count.text = countSubProduct.toString()

//        adapter.setDataList(data.subProductCaffeList)
//        adapter.notifyDataSetChanged()
//        itemView.recycler_sub_product_caffe.adapter = adapter
//        itemView.recycler_sub_product_caffe.layoutManager =
//          LinearLayoutManager(
//              itemView.recycler_sub_product_caffe.context, LinearLayoutManager.VERTICAL, false
//          )
//        itemView.recycler_sub_product_caffe.isNestedScrollingEnabled = false
//
//        mLayoutManager = itemView.recycler_sub_product_caffe.layoutManager
      } else {
        itemView.txt_category_name.text = data.title
        itemView.txt_category_count.text = count
        itemView.recycler_sub_product_caffe.visibility = View.GONE
      }
      itemView.btn_category_product.setOnClickListener {
        onItemClick?.invoke(adapterPosition)
      }
    }
  }
}
typealias onItemClick = ((position: Int) -> Unit)
