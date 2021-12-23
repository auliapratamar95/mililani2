package com.strategies360.mililani2.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.strategies360.mililani2.adapter.recycler.RecretionCenterAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.reservation.Reservation
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCenter
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.RecCenterListViewModel
import com.strategies360.mililani2.viewmodel.ReservationsListViewModel
import kotlinx.android.synthetic.main.fragment_rec_center.*

class RecCenterFragment : DataListFragment() {

  private val adapter = RecretionCenterAdapter()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
            .get(RecCenterListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_rec_center

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
  }

  @SuppressLint("SetTextI18n")
  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, {
      updateResource(it)
      if (viewModel.isLoadFinished) disableInfiniteScrolling()
    })
    viewModel.dataList.observe(viewLifecycleOwner, {

      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_rec_center
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      RecCenter.DiffUtilCallback(oldList, newList)
    }
    adapter.onItemPhoneClick = { _, data ->
      val browserIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.phone))
      startActivity(browserIntent)
    }
    return adapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchFromRemote()
  }
}