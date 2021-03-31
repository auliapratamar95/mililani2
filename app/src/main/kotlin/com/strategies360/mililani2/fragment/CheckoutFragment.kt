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
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.cart.Cart
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItems
import com.strategies360.mililani2.model.remote.caffe.checkout.PayTicket
import com.strategies360.mililani2.model.remote.caffe.checkout.PickupRequest
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.util.ValidationHelper
import com.strategies360.mililani2.viewmodel.CartListViewModel
import com.strategies360.mililani2.viewmodel.CheckoutViewModel
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import kotlinx.android.synthetic.main.fragment_checkout.btn_checkout
import kotlinx.android.synthetic.main.fragment_checkout.edit_card_number
import kotlinx.android.synthetic.main.fragment_checkout.edit_custom_tip
import kotlinx.android.synthetic.main.fragment_checkout.edit_cvv
import kotlinx.android.synthetic.main.fragment_checkout.edit_email
import kotlinx.android.synthetic.main.fragment_checkout.edit_expiration
import kotlinx.android.synthetic.main.fragment_checkout.edit_full_name
import kotlinx.android.synthetic.main.fragment_checkout.edit_name_on_card
import kotlinx.android.synthetic.main.fragment_checkout.edit_phone_number
import kotlinx.android.synthetic.main.fragment_checkout.edit_postal_code
import kotlinx.android.synthetic.main.fragment_checkout.edit_select_time
import kotlinx.android.synthetic.main.fragment_checkout.layout_content_cart
import kotlinx.android.synthetic.main.fragment_checkout.layout_no_tip
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_10
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_15
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_5
import kotlinx.android.synthetic.main.fragment_checkout.layout_tip_custom
import kotlinx.android.synthetic.main.fragment_checkout.progress_bar
import kotlinx.android.synthetic.main.fragment_checkout.recycler_cart
import kotlinx.android.synthetic.main.fragment_checkout.spinner_sample_product
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CheckoutFragment : DataListFragment(), View.OnClickListener {

  private var formatter: MaskedFormatter? = null

  private val adapter = CartAdapter()

  private var isCategoryName: Boolean? = false

  private var tip: Double = 0.0
  private var tip5: Double = 0.0
  private var tip10: Double = 0.0
  private var tip15: Double = 0.0

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CartListViewModel::class.java)
  }

  private val checkoutViewModel by lazy {
    ViewModelProviders.of(this)
        .get(CheckoutViewModel::class.java)
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
    layout_tip_custom.setOnClickListener(this)
    btn_checkout.setOnClickListener(this)

    setMask("##/##")
    initViewModel()
  }

  @SuppressLint("SetTextI18n")
  private fun initData(cart: Cart?) {
    tip5 = (cart?.total?.amount!! * 5) / 100
    tip10 = (cart.total?.amount!! * 10) / 100
    tip15 = (cart.total?.amount!! * 15) / 100

    txt_amount_tip_5.text = setFormatPriceValue(tip5)
    txt_amount_tip_10.text = setFormatPriceValue(tip10)
    txt_amount_tip_15.text = setFormatPriceValue(tip15)

    txt_subtotal.text = "$" + cart.subTotal?.amount.toString()
    txt_tax.text = "$" + cart.totalTax?.amount.toString()
    txt_order_total.text = "$" + cart.total?.amount.toString()
  }

  private fun initViewModel() {
    checkoutViewModel.resourcePickup.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onGetPickupLoading()
        Resource.SUCCESS -> onGetPickupSuccess(it.data.toString())
        Resource.ERROR -> onGetPickupFailure(it.error)
      }
    })

    checkoutViewModel.resourcePayTicket.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onGetPayTicketLoading()
        Resource.SUCCESS -> onGetPayTicketSuccess(it.data!!)
        Resource.ERROR -> onGetPayTicketFailure()
      }
    })

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

  private fun onGetPickupLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_content_cart.visibility = View.GONE
  }

  private fun onGetPickupSuccess(cookies: String) {
    checkoutViewModel.fetchPayTicketFromRemote(cookies)
  }

  private fun onGetPickupFailure(error: AppError) {
    progress_bar.visibility = View.GONE
    layout_content_cart.visibility = View.VISIBLE
    if (error.code == 400) Common.showMessageDialog(requireContext(), " We're sorry... ", "Given schedule time is not available")
  }

  private fun onGetPayTicketLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_content_cart.visibility = View.GONE
  }

  private fun onGetPayTicketSuccess(payTicket: PayTicket) {
    progress_bar.visibility = View.GONE
    layout_content_cart.visibility = View.VISIBLE

    val expiredDate = formatter?.formatString(edit_expiration.text.toString())?.unMaskedString

    checkoutViewModel.fetchSecurePaymentFromRemote(payTicket.username.toString(),  payTicket.action.toString(),
        payTicket.admin.toString(), payTicket.monetraReqSequence.toString(), payTicket.monetraReqTimestamp.toString(),
        payTicket.monetraReqHmacsha256.toString(), edit_card_number.text.toString(), edit_name_on_card.text.toString(),
        expiredDate.toString(), edit_cvv.text.toString(), edit_postal_code.text.toString(), edit_full_name.text.toString())
  }

  private fun onGetPayTicketFailure() {
    progress_bar.visibility = View.GONE
    layout_content_cart.visibility = View.VISIBLE
  }

  override fun onClick(view: View?) {
    if (view == layout_no_tip) {
      layout_no_tip.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.white))

      layout_tip_5.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
      edit_custom_tip.visibility = View.GONE
    } else if (view == layout_tip_5) {
      tip = tip5
      layout_no_tip.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.white))

      layout_tip_10.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
      edit_custom_tip.visibility = View.GONE
    } else if (view == layout_tip_10) {
      tip = tip10
      layout_no_tip.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.white))

      layout_tip_15.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
      edit_custom_tip.visibility = View.GONE
    } else if (view == layout_tip_15) {
      tip = tip15
      layout_no_tip.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background = requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.white))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.white))

      layout_tip_custom.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.colorPrimary))
      edit_custom_tip.visibility = View.GONE
    } else if (view == layout_tip_custom) {
      layout_no_tip.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_no_tip.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_5.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_5.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_10.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_10.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_15.background =
        requireContext().getDrawable(R.drawable.bg_button_round_grey_primary)
      txt_percent_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))
      txt_amount_tip_15.setTextColor(App.context.getColor(R.color.colorPrimary))

      layout_tip_custom.background =
        requireContext().getDrawable(R.drawable.bg_button_round_primary)
      txt_custom_tip.setTextColor(App.context.getColor(R.color.white))
      edit_custom_tip.visibility = View.VISIBLE
    } else if (view == edit_select_time) {
      openPickup()
    } else if (view == btn_checkout) {
      if (edit_custom_tip.visibility == View.VISIBLE) {
        val customTip = edit_custom_tip.text.toString()
        tip = customTip.toDouble()
      }
       if (!isCheckValidText()) {
        if (Hawk.contains(Constant.KEY_CUSTOMER_ID)) {
          val cookies: String = Common.getCookies()
          checkoutViewModel.fetchPickupFromRemote(cookies, getPickupRequest(), tip)
        }
      }
    }

  }

  private fun openPickup() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      CustomPickupFragment()
          .show(fragManager, "activities")
    }
  }

  private fun isCheckValidText(): Boolean {
    var isCheckValid = false

    if (edit_full_name.equals("")) {
      edit_full_name.error = "Full name is required."
      isCheckValid = true
    } else if (edit_phone_number.equals("")) {
      edit_phone_number.error = "Phone number is required."
      isCheckValid = true
    } else if (edit_phone_number.length() < 10) {
      edit_phone_number.error = "Phone number must be 10 digits."
      isCheckValid = true
    } else if (!ValidationHelper.isValidEmail(edit_email.text.toString())) {
      edit_email.error = "Email format is wrong."
      isCheckValid = true
    } else if (edit_name_on_card.equals("")) {
      edit_name_on_card.error = "Full name is required."
      isCheckValid = true
    } else if (edit_card_number.equals("")) {
      edit_card_number.error = "Card number is required."
      isCheckValid = true
    } else if (edit_expiration.equals("")) {
      edit_expiration.error = "Expiration Date is required."
      isCheckValid = true
    } else if (edit_cvv.equals("")) {
      edit_cvv.error = "CVC is required."
      isCheckValid = true
    } else if (edit_postal_code.equals("")) {
      edit_postal_code.error = "Postal Code is required."
      isCheckValid = true
    }

    return isCheckValid
  }

  @SuppressLint("SimpleDateFormat")
  private fun getPickupRequest(): PickupRequest {
    val pickupRequest = PickupRequest()
    val edFullName = edit_full_name.text.toString()
    val edEmail = edit_email.text.toString()
    val edPhone = edit_phone_number.text.toString()
    val edPickupTime = spinner_sample_product.selectedItem.toString()

    val sdf = SimpleDateFormat("HH:mm")
    val sdfs = SimpleDateFormat("hh:mm aa")
    val dt: Date = sdfs.parse(edPickupTime);
    val time1: String = sdf.format(dt)

    pickupRequest.fullName = edFullName
    pickupRequest.email = edEmail
    pickupRequest.phone = edPhone
    pickupRequest.pickupDate = null
    pickupRequest.pickupTime = time1

    return pickupRequest
  }

  private fun setFormatPriceValue(priceValue: Double): String {
    val formatter = DecimalFormat("#.##")
    formatter.roundingMode = RoundingMode.CEILING
    return formatter.format(priceValue)
  }

  private fun setMask(mask: String) {
    formatter = MaskedFormatter(mask)
    formatter?.let{
      edit_expiration.addTextChangedListener(MaskedWatcher(it, edit_expiration))
    }
  }
}