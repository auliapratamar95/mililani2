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
import com.strategies360.mililani2.adapter.recycler.NotificationAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.notification.Notification
import com.strategies360.mililani2.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : DataListFragment() {

  private val adapter = NotificationAdapter()
  private val viewModel by lazy {
    ViewModelProviders.of(this)
      .get(NotificationViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_notification

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

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      updateResource(it)
      if (viewModel.isLoadFinished) disableInfiniteScrolling()
    })
    viewModel.dataList.observe(viewLifecycleOwner, Observer {

      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_notification
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      Notification.DiffUtilCallback(oldList, newList)
    }
    return adapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchData()
  }
}