package com.strategies360.mililani2.fragment

import android.annotation.SuppressLint
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
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.CartAdapter
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.fragment.core.DataListFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.cart.Cart
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItems
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CartListViewModel
import kotlinx.android.synthetic.main.fragment_checkout.btn_checkout
import kotlinx.android.synthetic.main.fragment_checkout.edit_select_time
import kotlinx.android.synthetic.main.fragment_checkout.layout_content_cart
import kotlinx.android.synthetic.main.fragment_checkout.layout_no_tip
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_10
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_15
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_5
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_custom
import kotlinx.android.synthetic.main.fragment_checkout.progress_bar
import kotlinx.android.synthetic.main.fragment_checkout.recycler_cart
import kotlinx.android.synthetic.main.fragment_checkout.txt_amount_tip_10
import kotlinx.android.synthetic.main.fragment_checkout.txt_amount_tip_15
import kotlinx.android.synthetic.main.fragment_checkout.txt_amount_tip_5
import kotlinx.android.synthetic.main.fragment_checkout.txt_custom_tip
import kotlinx.android.synthetic.main.fragment_checkout.txt_no_tip
import kotlinx.android.synthetic.main.fragment_checkout.txt_order_total
import kotlinx.android.synthetic.main.fragment_checkout.txt_percent_tip_10
import kotlinx.android.synthetic.main.fragment_checkout.txt_percent_tip_15
import kotlinx.android.synthetic.main.fragment_checkout.txt_percent_tip_5
import kotlinx.android.synthetic.main.fragment_checkout.txt_subtotal
import kotlinx.android.synthetic.main.fragment_checkout.txt_tax

class CheckoutFragment : DataListFragment(), View.OnClickListener {

  private val adapter = CartAdapter()

  private var isCategoryName: Boolean? = false

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CartListViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_checkout

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Hawk.put((Constant.IS_FLAG_BUTTON_DELETE), false)
    initView()
  }

  private fun initView() {
    edit_select_time.setOnClickListener(this)
    layout_no_tip.setOnClickListener(this)
    layout_tip_5.setOnClickListener(this)
    layout_tip_10.setOnClickListener(this)
    layout_tip_15.setOnClickListener(this)

    btn_checkout.setOnClickListener{
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
    adapter.emptyText = resources.getString(R.string.info_no_data)
    adapter.onItemCartClick = { pos, data ->
      val categoryId: String = Hawk.get(Constant.KEY_CUSTOMER_ID)
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
      val customerId: String = Hawk.get(Constant.KEY_CUSTOMER_ID)
      viewModel.fetchData(customerId)
    } else {
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

  @SuppressLint("SetTextI18n")
  private fun initData(cart: Cart?) {
    txt_subtotal.text = "$" + cart?.subTotal?.amount.toString()
    txt_tax.text = "$" + cart?.totalTax?.amount.toString()
    txt_order_total.text = "$" + cart?.total?.amount.toString()
  }

  override fun onClick(view: View?) {
    if (view == layout_no_tip) {
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.white))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
    } else if (view == layout_tip_5) {
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.white))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
    } else if (view == layout_tip_10) {
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.white))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
    } else if (view == layout_tip_15) {
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.white))

      layout_tip_custom.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
    } else if (view == layout_tip_custom){
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.white))
    } else if (view == edit_select_time) {
      openPickup()
    }

  }

  private fun openPickup() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      CustomPickupFragment()
          .show(fragManager, "activities")
    }
  }
}