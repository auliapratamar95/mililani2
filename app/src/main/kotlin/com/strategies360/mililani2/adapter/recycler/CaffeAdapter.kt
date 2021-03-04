package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.FIT_CENTER
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeDetail
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_caffe.view.btn_open_category_details_product
import kotlinx.android.synthetic.main.adapter_caffe.view.img_product
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_description
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_price
import kotlinx.android.synthetic.main.adapter_caffe.view.txt_title

class CaffeAdapter : DataListRecyclerViewAdapter<ProductCaffeDetail, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  var onCategoryDetailProductClick: onCategoryDetailProductClick? = null

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
      val plainText : String =
        HtmlCompat.fromHtml(data.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

      itemView.txt_title.text = data.title
      itemView.txt_description.text = "$plainText..."
      itemView.img_product.scaleType = FIT_CENTER
      itemView.img_product.load(Constant.URL_IMAGE + data.imgProduct){
        transformations(RoundedCornersTransformation(5f))
        crossfade(750)
        scale(Scale.FIT)
      }
      itemView.txt_price.text = data.price
      itemView.btn_open_category_details_product.setOnClickListener {
        Hawk.delete(Constant.SKU_LIST)
        Hawk.delete(Constant.REQUIRED_CHOICE_PRODUCT)
        Hawk.delete(Constant.MODIFIER_CHOICE_PRODUCT)

        Hawk.put((Constant.KEY_ID_PRODUCT),data.id)
        onCategoryDetailProductClick?.invoke(adapterPosition)
      }
    }
  }
}

typealias onCategoryDetailProductClick = ((position: Int) -> Unit)