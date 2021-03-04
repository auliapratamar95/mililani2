package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.CategoryProductDetailActivity
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.eventbus.EventFlagGetProductCaffe
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeDetail
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import kotlinx.android.synthetic.main.fragment_caffe.recycler_caffe
import kotlinx.android.synthetic.main.fragment_caffe.txt_title_category
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

class CaffeFragment : DataListFragment() {

  private val adapter = CaffeAdapter()

  private var isCategoryName: Boolean? = false

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CaffeListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_caffe

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initView()
  }

  private fun initView() {
    initViewModel()
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  private fun initViewModel() {
    if (isCategoryName == false) {
      viewModel.categoryName.observe(viewLifecycleOwner, Observer {
        txt_title_category.text = it
      })
    }
    viewModel.dataProductList.observe(viewLifecycleOwner, Observer {
      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_caffe
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      ProductCaffeDetail.DiffUtilCallback(oldList, newList)
    }
    adapter.onCategoryDetailProductClick = {
      CategoryProductDetailActivity.launchIntent(requireContext())
    }
    return adapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchProductFromLocal()
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onGetDataProductCaffe(event: EventFlagGetProductCaffe) {
    if (event.isGetProductCaffe) {
      isCategoryName = true
      txt_title_category.text = event.categoryName
      adapter.notifyDataSetChanged()
      viewModel.fetchProductFromLocal()
      EventBus.getDefault().removeStickyEvent(event)
    }
  }
}