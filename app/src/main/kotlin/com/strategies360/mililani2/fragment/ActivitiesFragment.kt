package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.ProfileMtaActivity
import com.strategies360.mililani2.adapter.recycler.ActivitiesAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.mtaCard.Classes
import com.strategies360.mililani2.viewmodel.AllClassesListViewModel
import kotlinx.android.synthetic.main.fragment_activities.btn_back
import kotlinx.android.synthetic.main.fragment_activities.btn_filter
import kotlinx.android.synthetic.main.fragment_activities.recycler_activities
import kotlinx.android.synthetic.main.fragment_home.btn_scan_barcode

class ActivitiesFragment : DataListFragment() {

  private val adapter = ActivitiesAdapter()

  private val viewModel by lazy { ViewModelProviders.of(this).get(AllClassesListViewModel::class.java) }

  override val viewRes: Int = R.layout.fragment_activities

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()

    Hawk.put(resources.getString(R.string.flag_on_back_menu_home_status_checked), 1)

    btn_filter.setOnClickListener{
      openBottomFilter()
    }

    btn_back.setOnClickListener {
      openProfile()
    }

    btn_scan_barcode.setOnClickListener {
      openBottomCardList()
    }
  }

  private fun openProfile() {
    ProfileMtaActivity.launchIntent(requireContext())
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
      Classes.DiffUtilCallback(oldList, newList)
    }
    return adapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchData()
  }

  private fun openBottomFilter() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      CustomFilterBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }
}