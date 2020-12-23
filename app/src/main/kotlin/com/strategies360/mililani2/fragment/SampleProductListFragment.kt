package com.strategies360.mililani2.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.SampleProductAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.remote.product.SampleProduct
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.viewmodel.SampleProductListViewModel
import kotlinx.android.synthetic.main.fragment_sample_product_list.*

/**
 *
 * A sample data list fragment.
 */
class SampleProductListFragment : DataListFragment() {

    private val adapter = SampleProductAdapter()

    private val viewModel by lazy { ViewModelProviders.of(this).get(SampleProductListViewModel::class.java) }

    override val viewRes: Int? = R.layout.fragment_sample_product_list

    override fun initAppBarLayout(): AppBarLayout? {
        return appbar_data_list
    }

    override fun initSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipe_refresh_data_list
    }

    override fun initRecyclerView(): RecyclerView {
        return recycler_data_list
    }

    @Suppress("UNCHECKED_CAST")
    override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder> {
        adapter.emptyText = resources.getString(R.string.info_no_data)
        adapter.onProductClick = { data, position ->
            Common.showToast("Pos: $position; Data: $data")
        }
        adapter.setDiffUtilNotifier { oldList, newList ->
            SampleProduct.DiffUtilCallback(oldList, newList)
        }
        return adapter as DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>
    }

    override fun initRecyclerLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        // Initial behaviour
        enableSwipeToRefresh()
        enableInfiniteScrolling()

        // View bindings
        txt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                adapter.setDataListFilter { it.name != null && it.name!!.contains(s.toString(), ignoreCase = true) }

//                // Or use the normal implementation
//                adapter.setDataListFilter(object : DataListRecyclerViewAdapter.DataListFilter<SampleProduct> {
//                    override fun performDataListFiltering(data: SampleProduct): Boolean {
//                        return data.name != null && data.name!!.contains(s.toString(), ignoreCase = true)
//                    }
//                })
            }
        })
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

    override fun fetchData() {
        viewModel.fetchData()
    }

    override fun onSwipeToRefresh() {
        viewModel.onRefresh()
        enableInfiniteScrolling()
        fetchData()
    }
}
