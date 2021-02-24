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
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter
import com.strategies360.mililani2.adapter.recycler.SubProductCaffeAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.eventbus.EventFlagGetSubProductCaffe
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.caffe.SubProductCaffe
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import kotlinx.android.synthetic.main.fragment_sub_caffe.recycler_sub_caffe
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

class SubCaffeFragment : DataListFragment() {

  private val adapter = CaffeAdapter()
  private val subAdapter = SubProductCaffeAdapter()
  private val viewModel by lazy {
    ViewModelProviders.of(this)
            .get(CaffeListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_sub_caffe

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
    viewModel.resourceProductCaffe.observe(viewLifecycleOwner, Observer {
      updateResource(it)
      if (viewModel.isLoadFinished) disableInfiniteScrolling()
    })
    viewModel.dataSubProductCaffeList.observe(viewLifecycleOwner, Observer {

      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_sub_caffe
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    subAdapter.emptyText = resources.getString(R.string.info_no_data)
    subAdapter.setDiffUtilNotifier { oldList, newList ->
      SubProductCaffe.DiffUtilCallback(oldList, newList)
    }
    return subAdapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchSubProductFromLocal()
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onGetDataSubProductCaffe(event: EventFlagGetSubProductCaffe) {
    if (event.isGetProductCaffe) {
      adapter.notifyDataSetChanged()
      viewModel.fetchSubProductFromLocal()
      EventBus.getDefault().removeStickyEvent(event)
    }
  }
}