package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.CaffeAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import kotlinx.android.synthetic.main.fragment_all_activities.*

class CaffeFragment : DataListFragment() {

  private val adapter = CaffeAdapter()

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
    return recycler_activities
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      Caffe.DiffUtilCallback(oldList, newList)
    }
    return adapter as DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    if (Hawk.contains(Constant.KEY_ID_CATEGORY)){
      val categoryId: String = Hawk.get((Constant.KEY_ID_CATEGORY))
      viewModel.fetchData(categoryId, 0, 100)
    }
  }
}