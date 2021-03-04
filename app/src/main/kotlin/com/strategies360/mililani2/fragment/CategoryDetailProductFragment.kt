package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.CategoryDetailsProductAdapter
import com.strategies360.mililani2.adapter.recycler.CategoryModifiersGroupAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.remote.caffe.ModifierGroups
import com.strategies360.mililani2.model.remote.caffe.ProductOptions
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CategoryDetailsProductViewModel
import kotlinx.android.synthetic.main.fragment_category_product_detail.recycler_modifier_groups
import kotlinx.android.synthetic.main.fragment_category_product_detail.recycler_product_options

class CategoryDetailProductFragment : CoreFragment() {

  private var isBottomCardList = false
  private var customAdapter = CategoryDetailsProductAdapter()
  private var adapterModifiersGroup = CategoryModifiersGroupAdapter()

  private val dataList = ArrayList<ProductOptions>()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CategoryDetailsProductViewModel::class.java)
  }

  override val viewRes: Int = layout.fragment_category_product_detail

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

//    initView()
  }

  private fun initView() {
    if (Hawk.contains(Constant.KEY_ID_PRODUCT)) {
      val productId: String = Hawk.get(Constant.KEY_ID_PRODUCT)
      viewModel.fetchData(productId)
      initViewModel()
    }
  }

//  override fun initRecyclerView(): RecyclerView {
//    return recycler_product_options
//  }
//
//  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
//    customAdapter.emptyText = resources.getString(R.string.info_no_data)
//    customAdapter.setDiffUtilNotifier { oldList, newList ->
//      ProductOptions.DiffUtilCallback(oldList, newList)
//    }
//    return customAdapter as DataListRecyclerViewAdapter<Any, ViewHolder>
//  }
//
//  override fun initRecyclerLayoutManager(): LayoutManager {
//    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//  }

//  override fun fetchData() {
//    if (Hawk.contains(Constant.KEY_ID_PRODUCT)) {
//      val productId: String = Hawk.get(Constant.KEY_ID_PRODUCT)
//      viewModel.fetchData(productId)
//    }
//  }

  private fun initViewModel() {
//    viewModel.dataList.observe(viewLifecycleOwner, Observer {
//      updateResource(it)
//      if (viewModel.isLoadFinished) disableInfiniteScrolling()
//    })
    viewModel.dataList.observe(viewLifecycleOwner, Observer {
      initRecyclerCategory(it)
    })

    viewModel.dataModifiersGroupList.observe(viewLifecycleOwner, Observer {
      initRecyclerModifiersGroup(it)
    })
    lifecycle.addObserver(viewModel)
  }

  private fun initRecyclerCategory(categoryProductList: ArrayList<ProductOptions>) {
    customAdapter.setDataList(categoryProductList)
    recycler_product_options.adapter = customAdapter
    recycler_product_options.layoutManager =
      LinearLayoutManager(
          recycler_product_options.context, LinearLayoutManager.VERTICAL, false
      )

    recycler_product_options.isNestedScrollingEnabled = false
  }

  private fun initRecyclerModifiersGroup(categoryProductList: ArrayList<ModifierGroups>) {
    adapterModifiersGroup.setDataList(categoryProductList)
    recycler_modifier_groups.adapter = adapterModifiersGroup
    recycler_modifier_groups.layoutManager =
      LinearLayoutManager(
          recycler_modifier_groups.context, LinearLayoutManager.VERTICAL, false
      )

    recycler_modifier_groups.isNestedScrollingEnabled = false
  }
}