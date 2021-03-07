package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategoryModifiersGroupAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.eventbus.EventPriceProduct
import com.strategies360.mililani2.model.remote.caffe.ModifierChoiceChecked
import com.strategies360.mililani2.model.remote.caffe.ModifierGroups
import com.strategies360.mililani2.model.remote.caffe.Modifiers
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_category_modifiers_groups.view.btn_sub_category_detail_product
import kotlinx.android.synthetic.main.adapter_category_modifiers_groups.view.recycler_sub_category_modifiers_group
import kotlinx.android.synthetic.main.adapter_category_modifiers_groups.view.txt_name_detail_product
import org.greenrobot.eventbus.EventBus

class CategoryModifiersGroupAdapter : DataListRecyclerViewAdapter<ModifierGroups, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  var mLayoutManager: LayoutManager? = null
  var listModifierChoiceChecked: ArrayList<ModifierChoiceChecked> = ArrayList()

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_category_modifiers_groups))
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
      itemView.btn_sub_category_detail_product.setOnClickListener {
        if (itemView.recycler_sub_category_modifiers_group.visibility == View.GONE) {
          itemView.recycler_sub_category_modifiers_group.visibility = View.VISIBLE
        } else {
          itemView.recycler_sub_category_modifiers_group.visibility = View.GONE
        }
      }

      val adapter = SubModifiersGroupAdapter()
      itemView.recycler_sub_category_modifiers_group.layoutManager =
        LinearLayoutManager(
            itemView.recycler_sub_category_modifiers_group.context, LinearLayoutManager.VERTICAL,
            false
        )

      itemView.recycler_sub_category_modifiers_group.isNestedScrollingEnabled = false
      mLayoutManager = itemView.recycler_sub_category_modifiers_group.layoutManager

      adapter.setDataList(data.modifiersList)
      adapter.onCategoryModifierDetailProductClick = { isAddFlagAction, modifier ->
        if (Hawk.contains(Constant.MODIFIER_CHOICE_PRODUCT)) {
          listModifierChoiceChecked = Hawk.get(Constant.MODIFIER_CHOICE_PRODUCT)
          if (listModifierChoiceChecked.size != 0) {
            if (isAddFlagAction) {
              setDataModifiers(data, modifier)
            } else {
              for (i in listModifierChoiceChecked.indices) {
                if (listModifierChoiceChecked[i].name == modifier.name) {
                  listModifierChoiceChecked.remove(listModifierChoiceChecked[i])
                  break
                }
              }
              Hawk.put((Constant.MODIFIER_CHOICE_PRODUCT), listModifierChoiceChecked)
            }
          } else {
            setDataModifiers(data, modifier)
          }
        } else {
          setDataModifiers(data, modifier)
        }
        EventBus.getDefault().postSticky(EventPriceProduct(true))
      }
      itemView.recycler_sub_category_modifiers_group.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun setDataModifiers(data: ModifierGroups, modifier: Modifiers) {
    if (Hawk.contains(Constant.MODIFIER_CHOICE_PRODUCT)) {
      val listModifierChoiceChecked: ArrayList<ModifierChoiceChecked> = Hawk.get(Constant.MODIFIER_CHOICE_PRODUCT)
      val modifierChoiceChecked = ModifierChoiceChecked()
      modifierChoiceChecked.modifierGroupId = data.id
      modifierChoiceChecked.category = data.name
      modifierChoiceChecked.id = modifier.id
      modifierChoiceChecked.name = modifier.name
      modifierChoiceChecked.amount = modifier.prices?.defaultPrices?.amount
      listModifierChoiceChecked.add(modifierChoiceChecked)
      Hawk.put((Constant.MODIFIER_CHOICE_PRODUCT), listModifierChoiceChecked)
    } else {
      val listModifierChoiceChecked: ArrayList<ModifierChoiceChecked> = ArrayList()
      val modifierChoiceChecked = ModifierChoiceChecked()
      modifierChoiceChecked.modifierGroupId = data.id
      modifierChoiceChecked.category = data.name
      modifierChoiceChecked.id = modifier.id
      modifierChoiceChecked.name = modifier.name
      modifierChoiceChecked.amount = modifier.prices?.defaultPrices?.amount
      listModifierChoiceChecked.add(modifierChoiceChecked)
      Hawk.put((Constant.MODIFIER_CHOICE_PRODUCT), listModifierChoiceChecked)
    }
  }
}