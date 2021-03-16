package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategoryDetailsProductAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.eventbus.EventPriceProduct
import com.strategies360.mililani2.model.remote.caffe.AllowedValues
import com.strategies360.mililani2.model.remote.caffe.ProductOptions
import com.strategies360.mililani2.model.remote.caffe.RequiredChoiceChecked
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_category_detail_product.view.btn_category_detail
import kotlinx.android.synthetic.main.adapter_category_detail_product.view.ic_plus_min_date
import kotlinx.android.synthetic.main.adapter_category_detail_product.view.recycler_sub_category_product_detail
import kotlinx.android.synthetic.main.adapter_category_detail_product.view.txt_name_detail_product
import org.greenrobot.eventbus.EventBus

class CategoryDetailsProductAdapter : DataListRecyclerViewAdapter<ProductOptions, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false
//  private val isVisibleRecycler = false

  var mLayoutManager: LayoutManager? = null

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_category_detail_product))
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

      itemView.txt_name_detail_product.text = data.name
      val adapter = SubCategoryDetailsProductAdapter()
      itemView.recycler_sub_category_product_detail.layoutManager =
        LinearLayoutManager(
            itemView.recycler_sub_category_product_detail.context, LinearLayoutManager.VERTICAL,
            false
        )

      itemView.btn_category_detail.setOnClickListener {
        if (itemView.recycler_sub_category_product_detail.visibility == View.GONE) {
          itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_min))
          itemView.recycler_sub_category_product_detail.visibility = View.VISIBLE
        } else {
          itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_plus))
          itemView.recycler_sub_category_product_detail.visibility = View.GONE
        }
      }

      itemView.recycler_sub_category_product_detail.isNestedScrollingEnabled = false
      mLayoutManager = itemView.recycler_sub_category_product_detail.layoutManager

      adapter.setDataList(data.allowedValuesList)
      adapter.onSubCategoryDetailProductClick = { pos, allowedValues ->
        var isRequieredChecked = false
        var listRequiredCheckedProduct: ArrayList<RequiredChoiceChecked> = ArrayList()
        if (Hawk.contains(Constant.REQUIRED_CHOICE_PRODUCT)) {
          listRequiredCheckedProduct = Hawk.get(Constant.REQUIRED_CHOICE_PRODUCT)
          if (listRequiredCheckedProduct.size != 0) {
            for (i in listRequiredCheckedProduct.indices) {
              if (listRequiredCheckedProduct[i].category == data.name) {
                isRequieredChecked = false
                listRequiredCheckedProduct.remove(listRequiredCheckedProduct[i])
                    setDataRequiredChecked(listRequiredCheckedProduct, data.name, allowedValues)
                break
              } else {
                isRequieredChecked = true
              }
            }

            if (isRequieredChecked) {
              setDataRequiredChecked(listRequiredCheckedProduct, data.name, allowedValues)
            }
          } else {
            setDataRequiredChecked(listRequiredCheckedProduct, data.name, allowedValues)
          }
        } else {
          setDataRequiredChecked(listRequiredCheckedProduct, data.name, allowedValues)
        }

        EventBus.getDefault().postSticky(EventPriceProduct(true))
      }
      itemView.recycler_sub_category_product_detail.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun setDataRequiredChecked(listRequiredCheckedProduct: ArrayList<RequiredChoiceChecked>, category: String?, allowedValues: AllowedValues) {
    val requiredChoiceChecked = RequiredChoiceChecked()

    if (listRequiredCheckedProduct.size != 0) {
      requiredChoiceChecked.totalRequired = getFilteredDataList().size
      requiredChoiceChecked.category = category
      requiredChoiceChecked.id = allowedValues.id
      requiredChoiceChecked.name = allowedValues.name
      requiredChoiceChecked.amount = allowedValues.amount

      listRequiredCheckedProduct.add(requiredChoiceChecked)
      Hawk.put((Constant.REQUIRED_CHOICE_PRODUCT), listRequiredCheckedProduct)
    } else {
      val listFirstEmpty: ArrayList<RequiredChoiceChecked> = ArrayList()
      requiredChoiceChecked.totalRequired = getFilteredDataList().size
      requiredChoiceChecked.category = category
      requiredChoiceChecked.id = allowedValues.id
      requiredChoiceChecked.name = allowedValues.name
      requiredChoiceChecked.amount = allowedValues.amount

      listFirstEmpty.add(requiredChoiceChecked)
      Hawk.put((Constant.REQUIRED_CHOICE_PRODUCT), listFirstEmpty)
    }
  }
}