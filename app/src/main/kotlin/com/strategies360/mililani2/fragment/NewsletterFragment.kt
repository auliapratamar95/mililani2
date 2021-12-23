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
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.NewsletterAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.newsletter.Newsletter
import com.strategies360.mililani2.viewmodel.NewsletterViewModel
import kotlinx.android.synthetic.main.fragment_newsletter.*


class NewsletterFragment : DataListFragment() {
  var mLayoutManager: RecyclerView.LayoutManager? = null
  private val adapter = NewsletterAdapter()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
      .get(NewsletterViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_newsletter

  @SuppressLint("SetTextI18n")
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
    viewModel.resource.observe(viewLifecycleOwner, {
      updateResource(it)
      if (viewModel.isLoadFinished) disableInfiniteScrolling()
    })
    viewModel.dataList.observe(viewLifecycleOwner, {

      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder> {
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.setDiffUtilNotifier { oldList, newList ->
      Newsletter.DiffUtilCallback(oldList, newList)
    }
    adapter.onItemNewsClick = { _, data ->
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.mobileData?.objectPdf?.url))
        startActivity(browserIntent)
      }
    return adapter as DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>
  }

  override fun initRecyclerLayoutManager(): RecyclerView.LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchData()
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_newsletter
  }
}