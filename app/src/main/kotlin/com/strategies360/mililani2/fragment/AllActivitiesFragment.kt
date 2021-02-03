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
import com.strategies360.mililani2.adapter.recycler.AllActivitiesAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.mtaCard.Classes
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.AllClassesListViewModel
import kotlinx.android.synthetic.main.fragment_all_activities.btn_filter
import kotlinx.android.synthetic.main.fragment_all_activities.btn_open_profile
import kotlinx.android.synthetic.main.fragment_all_activities.btn_scan_barcode
import kotlinx.android.synthetic.main.fragment_all_activities.recycler_activities

class AllActivitiesFragment : DataListFragment(), View.OnClickListener {

  private val adapter = AllActivitiesAdapter()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(AllClassesListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_all_activities

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initView()
  }

  private fun initView() {
    Hawk.put((Constant.FLAG_ON_BACK_MENU), true)
    initViewModel()

    btn_filter.setOnClickListener(this)
    btn_open_profile.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
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
    return adapter as DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>
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

  private fun openProfile() {
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
    ProfileMtaActivity.launchIntent(requireContext())
  }

  override fun onClick(v: View?) {
    when (v) {
      btn_filter -> {
        openBottomFilter()
      }
      btn_open_profile -> {
        openProfile()
      }
      btn_scan_barcode -> {
        openBottomCardList()
      }
    }
  }
}