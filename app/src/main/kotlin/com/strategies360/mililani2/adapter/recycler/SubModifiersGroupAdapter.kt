package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.SubModifiersGroupAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.caffe.Modifiers
import kotlinx.android.synthetic.main.adapter_sub_modifiers_group.view.btn_checked
import kotlinx.android.synthetic.main.adapter_sub_modifiers_group.view.img_checked
import kotlinx.android.synthetic.main.adapter_sub_modifiers_group.view.img_unchecked
import kotlinx.android.synthetic.main.adapter_sub_modifiers_group.view.txt_name
import kotlinx.android.synthetic.main.adapter_sub_modifiers_group.view.txt_price

class SubModifiersGroupAdapter : DataListRecyclerViewAdapter<Modifiers, ViewHolder>() {

  var onCategoryModifierDetailProductClick: onCategoryModifierDetailProductClick? = null

  private var tmpPosition = 0
  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_sub_modifiers_group))
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
      if (data.prices?.defaultPrices?.amount?.equals(0.00) == true) {
        itemView.txt_name.text = data.name
        itemView.txt_price.text = "0"
      } else {
        itemView.txt_name.text = data.name
        itemView.txt_price.text = "$" + data.prices?.defaultPrices?.amount.toString() + "0"
      }

//      itemView.cb_required_category.setOnCheckedChangeListener { buttonView, isChecked ->
//        val amount = data.prices?.defaultPrices?.amount
//        EventBus.getDefault().postSticky(EventPriceProduct(amount!!))
//      }

      itemView.btn_checked.setOnClickListener {
        if (itemView.img_checked.visibility == View.VISIBLE) {
          itemView.img_checked.visibility = View.GONE
          itemView.img_unchecked.visibility = View.VISIBLE

          onCategoryModifierDetailProductClick?.invoke(false, data)
        } else {
          itemView.img_checked.visibility = View.VISIBLE
          itemView.img_unchecked.visibility = View.GONE

          onCategoryModifierDetailProductClick?.invoke(true, data)
        }
      }

    }
  }
}

typealias onCategoryModifierDetailProductClick = ((isFlagAction: Boolean, data: Modifiers) -> Unit)
