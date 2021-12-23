package com.strategies360.mililani2.fragment

import android.annotation.SuppressLint
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
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.adapter.recycler.EventAdapter
import com.strategies360.mililani2.adapter.recycler.ReservationAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.reservation.Reservation
import com.strategies360.mililani2.model.remote.tickets.Events
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.EventsListViewModel
import com.strategies360.mililani2.viewmodel.ReservationsListViewModel
import kotlinx.android.synthetic.main.fragment_events_result.recycler_events
import kotlinx.android.synthetic.main.fragment_events_result.txt_total_result
import kotlinx.android.synthetic.main.fragment_reservation_result.*

class ResultReservartionFragment : DataListFragment() {

  private val adapter = ReservationAdapter()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
            .get(ReservationsListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_reservation_result

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
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onEventsResultLoading()
        Resource.ERROR -> onEventsResultFailure(it.error)
        Resource.SUCCESS -> onEventsResultSuccess()
      }
      if (viewModel.isLoadFinished) disableInfiniteScrolling()
    })
    viewModel.dataList.observe(viewLifecycleOwner, Observer {
      if (it.size != 0) {
        txt_total_result.text = getString(string.total_result) + " " + "(" + it.size.toString() + ")"
      }
      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_reservation
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      Reservation.DiffUtilCallback(oldList, newList)
    }
    return adapter as DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchFromLocal()
  }

  private fun onEventsResultLoading() {
    activity?.let {
      Common.showProgressDialog(it , onBackPress = {
        viewModel.cancel()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onEventsResultSuccess() {
    activity?.let {
      Common.dismissProgressDialog()
    }
  }

  private fun onEventsResultFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      Common.showMessageDialog(it, "Error", error.message)
    }
  }
}