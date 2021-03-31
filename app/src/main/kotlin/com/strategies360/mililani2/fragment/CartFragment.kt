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
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.CaffeActivity
import com.strategies360.mililani2.activity.CheckoutActivity
import com.strategies360.mililani2.adapter.recycler.CartAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.cart.Cart
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItems
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CartListViewModel
import kotlinx.android.synthetic.main.fragment_cart.btn_caffe
import kotlinx.android.synthetic.main.fragment_cart.btn_checkout
import kotlinx.android.synthetic.main.fragment_cart.btn_promo
import kotlinx.android.synthetic.main.fragment_cart.layout_content_cart
import kotlinx.android.synthetic.main.fragment_cart.progress_bar
import kotlinx.android.synthetic.main.fragment_cart.recycler_cart
import kotlinx.android.synthetic.main.fragment_cart.txt_cart_empty
import kotlinx.android.synthetic.main.fragment_cart.txt_order_total
import kotlinx.android.synthetic.main.fragment_cart.txt_subtotal
import kotlinx.android.synthetic.main.fragment_cart.txt_tax

@SuppressLint("SetTextI18n")
class CartFragment : DataListFragment() {

  private val adapter = CartAdapter()

  private var isCategoryName: Boolean? = false

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CartListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_cart

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Hawk.put((Constant.IS_FLAG_BUTTON_DELETE), true)
    initView()
  }

  private fun initView() {
    btn_caffe.setOnClickListener{
      CaffeActivity.launchIntent(requireContext())
    }

    btn_checkout.setOnClickListener {
      CheckoutActivity.launchIntent(requireContext())
    }

    btn_promo.setOnClickListener {
      DialogPromo().show(childFragmentManager, "dialog")
    }

    initViewModel()
  }
  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onGetCartLoading()
        Resource.SUCCESS -> onGetCartSuccess(it.data!!)
        Resource.ERROR -> onGetCartFailure()
      }
    })
    viewModel.dataList.observe(viewLifecycleOwner, Observer {
      updateDataList(it)
    })
    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_cart
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapter.emptyText = App.context.resources.getString(R.string.info_cart_empty)
    adapter.onItemCartClick = { pos, data ->
      val categoryId: String = Common.getCookies()
      viewModel.deleteCartFromRemote(data.id.toString(), categoryId)
    }
    adapter.setDiffUtilNotifier { oldList, newList ->
      OrderItems.DiffUtilCallback(oldList, newList)
    }
    return adapter as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    if (Hawk.contains(Constant.KEY_CUSTOMER_ID)) {
      val customerId: String = Common.getCookies()
      viewModel.fetchData(customerId)
    } else {
      txt_cart_empty.visibility = View.VISIBLE
      progress_bar.visibility = View.GONE

      btn_checkout.visibility = View.GONE
    }
  }

  private fun onGetCartLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_content_cart.visibility = View.GONE
  }

  private fun onGetCartSuccess(cartResponse: CartResponse) {
    progress_bar.visibility = View.GONE
    layout_content_cart.visibility = View.VISIBLE

    initData(cartResponse.cart)
  }

  private fun onGetCartFailure() {
    progress_bar.visibility = View.GONE
    layout_content_cart.visibility = View.VISIBLE
  }

  private fun initData(cart: Cart?) {
    if (cart != null) {
      if (cart.orderItems != null) {
        txt_subtotal.text = "$" + cart.subTotal?.amount.toString() + "0"
        txt_tax.text = "$" + cart.totalTax?.amount.toString() + "0"
        txt_order_total.text = "$" + cart.total?.amount.toString() + "0"
        btn_checkout.isEnabled = true
      } else {
        btn_checkout.isEnabled = false
      }
    }
  }
}