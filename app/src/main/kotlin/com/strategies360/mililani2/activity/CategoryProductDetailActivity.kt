package com.strategies360.mililani2.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.adapter.recycler.CategoryDetailsProductAdapter
import com.strategies360.mililani2.adapter.recycler.CategoryModifiersGroupAdapter
import com.strategies360.mililani2.eventbus.EventPriceProduct
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.CategoryDetailsProductResponse
import com.strategies360.mililani2.model.remote.caffe.DetailSkus
import com.strategies360.mililani2.model.remote.caffe.ModifierChoiceChecked
import com.strategies360.mililani2.model.remote.caffe.ModifierGroups
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse
import com.strategies360.mililani2.model.remote.caffe.ProductOptions
import com.strategies360.mililani2.model.remote.caffe.RequiredChoiceChecked
import com.strategies360.mililani2.model.remote.caffe.cart.CartModifiers
import com.strategies360.mililani2.model.remote.caffe.cart.CartRequest
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CategoryDetailsProductViewModel
import com.strategies360.mililani2.viewmodel.SubmitDataCartViewModel
import kotlinx.android.synthetic.main.activity_category_product_detail.appbar
import kotlinx.android.synthetic.main.activity_category_product_detail.btn_back
import kotlinx.android.synthetic.main.activity_category_product_detail.img_product
import kotlinx.android.synthetic.main.activity_category_product_detail.layout_list_detail_product_price
import kotlinx.android.synthetic.main.activity_category_product_detail.layout_progress_bar
import kotlinx.android.synthetic.main.activity_category_product_detail.txt_description
import kotlinx.android.synthetic.main.activity_category_product_detail.txt_title
import kotlinx.android.synthetic.main.activity_category_product_detail.txt_total_price
import kotlinx.android.synthetic.main.fragment_category_product_detail.recycler_modifier_groups
import kotlinx.android.synthetic.main.fragment_category_product_detail.recycler_product_options
import kotlinx.android.synthetic.main.layout_bottom_detail_product.btn_add_to_cart
import kotlinx.android.synthetic.main.layout_bottom_detail_product.btn_decrement
import kotlinx.android.synthetic.main.layout_bottom_detail_product.btn_increment
import kotlinx.android.synthetic.main.layout_bottom_detail_product.ed_special_intruction
import kotlinx.android.synthetic.main.layout_bottom_detail_product.txt_qty
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SetTextI18n")
class CategoryProductDetailActivity: CoreActivity(), View.OnClickListener {
  private var customAdapter = CategoryDetailsProductAdapter()
  private var adapterModifiersGroup = CategoryModifiersGroupAdapter()

  private var productId: String? = null

  private var isRequiredChecked: Boolean? = null
  private var isModifierChecked: Boolean? = null

  private var listSkus: ArrayList<DetailSkus> = ArrayList()
  private var price: Double? = null
  private var maxValue: Double? = null
  private var minValue: Double? = null
  private var tmpMaxValue: Double? = null
  private var tmpMinValue: Double? = null
  private var priceProduct: Double? = null

  private var currentQty = 1

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CategoryDetailsProductViewModel::class.java)
  }

  private val addCartViewModel by lazy {
    ViewModelProviders.of(this)
        .get(SubmitDataCartViewModel::class.java)
  }

  override val viewRes: Int
    get() = R.layout.activity_category_product_detail

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)
    appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
      //measuring for alpha
      btn_back.visibility = View.VISIBLE
    })

    val collapsingToolbarLayout =
      findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
    collapsingToolbarLayout.title = "  "

    collapsingToolbarLayout.setCollapsedTitleTextColor(
        ContextCompat.getColor(this, R.color.white)
    )
    collapsingToolbarLayout.setExpandedTitleColor(
        ContextCompat.getColor(this, R.color.transparent)
    )

    initView()
    initViewModel()
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  private fun initView() {
    btn_back.setOnClickListener(this)
    btn_decrement.setOnClickListener(this)
    btn_increment.setOnClickListener(this)
    btn_add_to_cart.setOnClickListener(this)

    if (Hawk.contains(Constant.KEY_ID_PRODUCT)) {
      val productId: String = Hawk.get(Constant.KEY_ID_PRODUCT)
      viewModel.fetchData(productId)
    }
  }

  private fun initViewModel() {
    addCartViewModel.liveData.observeForever{
      when (it?.status) {
        Resource.LOADING -> onCartLoading()
        Resource.SUCCESS -> onCartSuccess(it.data!!)
        Resource.ERROR -> onCartFailure(it.error)
      }
    }

    viewModel.resource.observeForever {
      when (it?.status) {
        Resource.LOADING -> onCategoryDetailProductLoading()
        Resource.SUCCESS -> onCategoryDetailProductSuccess(it.data!!)
        Resource.ERROR -> onCategoryDetailProductFailure(it.error)
      }
    }

    viewModel.dataList.observeForever {
      initRecyclerCategory(it)
    }

    viewModel.dataModifiersGroupList.observeForever {
      initRecyclerModifiersGroup(it)
    }
    lifecycle.addObserver(viewModel)
  }

  private fun onCartLoading() {
    Common.showProgressDialog(this)
  }

  @SuppressLint("SimpleDateFormat")
  private fun onCartSuccess(response: PayloadResponse) {
    Common.dismissProgressDialog()
    val costumerId: String = response.payload?.customer?.id.toString()
    val expiration = Date(System.currentTimeMillis() + 60 * 60 * 24000)
    val expires: String = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)
        .format(expiration)
    val cookie: String =
      "customerId=" + costumerId + "; " +
          "path=/; " +
          "expires=" + expires

    if (!Hawk.contains(Constant.KEY_CUSTOMER_ID)) Hawk.put((Constant.KEY_CUSTOMER_ID), cookie)
    CartActivity.launchIntent(this)
  }

  private fun onCartFailure(error: AppError) {
    Common.dismissProgressDialog()
  }

  private fun onCategoryDetailProductLoading() {
    layout_progress_bar.visibility = View.VISIBLE
    appbar.visibility = View.GONE
    layout_list_detail_product_price.visibility = View.GONE
  }

  private fun onCategoryDetailProductSuccess(response: CategoryDetailsProductResponse) {
    layout_progress_bar.visibility = View.GONE
    appbar.visibility = View.VISIBLE
    layout_list_detail_product_price.visibility = View.VISIBLE
    initData(response)
  }

  private fun onCategoryDetailProductFailure(error: AppError) {
    layout_progress_bar.visibility = View.GONE
    appbar.visibility = View.VISIBLE
    layout_list_detail_product_price.visibility = View.VISIBLE
  }

  private fun initData(response: CategoryDetailsProductResponse) {
    getMaxAndMinPrice(response.categoryDetailsProductResponse?.detailSkus!!)

    img_product.load(
        Constant.URL_IMAGE_DETAIL_PRODUCT + response.categoryDetailsProductResponse?.primaryMedia?.url
    )

    productId = response.categoryDetailsProductResponse?.id
    txt_title.text = response.categoryDetailsProductResponse?.fullName
    txt_description.text = HtmlCompat.fromHtml(
        response.categoryDetailsProductResponse?.description.toString(),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
  }

  private fun initRecyclerCategory(categoryProductList: ArrayList<ProductOptions>) {
    if (Hawk.contains(Constant.SKU_LIST)) {
      val skuList: ArrayList<DetailSkus> = Hawk.get(Constant.SKU_LIST)
      for (i in categoryProductList.indices) {
        if (categoryProductList[i].name == getString(string.size)) {
          if (Hawk.contains(Constant.SKU_LIST)) {
            for (x in categoryProductList[i].allowedValuesList?.indices!!) {
              val allowedValuesName = categoryProductList[i].allowedValuesList?.get(x)?.name?.toLowerCase()
              for (skuIndex in skuList.indices) {
                val skuName = skuList[skuIndex].skusName
                val parts = skuName?.split("-".toRegex())?.toTypedArray()
                var part1 = ""
                var part2 = ""
                var part3 = ""

                when {
                  parts?.size == 1 -> {
                    part1 = parts[0]
                  }
                  parts?.size == 2 -> {
                    part1 = parts[0]
                    part2 = parts[1]
                  }
                  parts?.size!! > 2 -> {
                    part1 = parts[0]
                    part2 = parts[1]
                    part3 = parts[2]
                  }
                }

                if (allowedValuesName == part1) {
                  categoryProductList[i].allowedValuesList?.get(x)?.amount = skuList[skuIndex].priceSkus?.amount
                  break
                } else if (allowedValuesName == part2) {
                  categoryProductList[i].allowedValuesList?.get(x)?.amount = skuList[skuIndex].priceSkus?.amount
                  break
                } else if (allowedValuesName == part3) {
                  categoryProductList[i].allowedValuesList?.get(x)?.amount = skuList[skuIndex].priceSkus?.amount
                  break
                }
              }
            }
          }
        }
      }
    }
    customAdapter.setDataList(categoryProductList)
    recycler_product_options.adapter = customAdapter
    recycler_product_options.layoutManager =
      LinearLayoutManager(
          recycler_product_options.context, LinearLayoutManager.VERTICAL, false
      )

    recycler_product_options.isNestedScrollingEnabled = false
  }

  private fun initRecyclerModifiersGroup(categoryProductList: ArrayList<ModifierGroups>) {
    adapterModifiersGroup.setDataList(categoryProductList)
    recycler_modifier_groups.adapter = adapterModifiersGroup
    recycler_modifier_groups.layoutManager =
      LinearLayoutManager(
          recycler_modifier_groups.context, LinearLayoutManager.VERTICAL, false
      )
    recycler_modifier_groups.isNestedScrollingEnabled = false
  }

  @SuppressLint("SetTextI18n")
  @Subscribe(sticky = true, threadMode = MAIN)
  fun onSetPriceProduct(event: EventPriceProduct) {
    var amountRequired = 0.0
    var amountModifier = 0.0
    val qty: String = txt_qty.text.toString()
    var totalProduct = 0.0
    var totalMinProduct = 0.0
    var totalMaxProduct = 0.0

    if (event.isUpdateCart) {
      if (Hawk.contains(Constant.REQUIRED_CHOICE_PRODUCT)) {
        val listRequiredData: ArrayList<RequiredChoiceChecked> = Hawk.get(
            Constant.REQUIRED_CHOICE_PRODUCT
        )
        if (listRequiredData.size != 0) {
          if (listRequiredData.size >= listRequiredData[0].totalRequired!!) {
            isRequiredChecked = true
            for (i in listRequiredData.indices) {
              if (listRequiredData[i].amount != null) {
                amountRequired += listRequiredData[i].amount!!
              }
            }
          } else {
            isRequiredChecked = false
          }
        } else {
          isRequiredChecked = false
        }
      } else {
        isRequiredChecked = false
      }

      if (Hawk.contains(Constant.MODIFIER_CHOICE_PRODUCT)) {
        val listModifierChoiceChecked: ArrayList<ModifierChoiceChecked> = Hawk.get(
            Constant.MODIFIER_CHOICE_PRODUCT
        )
        if (listModifierChoiceChecked.size != 0) {
          isModifierChecked = true
          for (i in listModifierChoiceChecked.indices) {
            amountModifier += listModifierChoiceChecked[i].amount!!
          }
        } else {
          isModifierChecked = false
        }
      } else {
        isModifierChecked = false
      }
    }

    if (!isRequiredChecked!! && isModifierChecked == true) {
      tmpMaxValue = amountModifier + maxValue!!
      tmpMinValue = amountModifier + minValue!!

      totalMinProduct = tmpMinValue!! * qty.toInt()
      totalMaxProduct = tmpMaxValue!! * qty.toInt()

      btn_add_to_cart.text = getString(string.add_to_cart) + " " + "$" +
          setFormatPriceValue(totalMinProduct!!) + " - " + "$" + setFormatPriceValue(totalMaxProduct!!)
    } else if (isRequiredChecked == true && isModifierChecked == true) {
      priceProduct = amountRequired + amountModifier
      totalProduct = priceProduct!! * qty.toInt()

      btn_add_to_cart.text = getString(string.add_to_cart) + " - $" + setFormatPriceValue(
          totalProduct!!
      )
    } else if (isRequiredChecked == true && !isModifierChecked!!) {
      priceProduct = amountRequired
      totalProduct = priceProduct!! * qty.toInt()

      btn_add_to_cart.text = getString(string.add_to_cart) + " - $" + setFormatPriceValue(
          totalProduct
      )
    } else {
      btn_add_to_cart.text = getString(string.add_to_cart)
    }
    EventBus.getDefault().removeStickyEvent(event)
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, CategoryProductDetailActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }

  @SuppressLint("SetTextI18n")
  override fun onClick(view: View?) {
    when (view) {
      btn_decrement -> {
        getPriceOnClick(false)
      }
      btn_increment -> {
        getPriceOnClick(true)
      }
      btn_back -> {
        onBackPressed()
      }
      btn_add_to_cart -> {
        btn_add_to_cart.isEnabled = false
        generateCartRequestModel()
      }
    }
  }

  private fun setFormatPriceValue(priceValue: Double): String {
    val formatter = DecimalFormat("#.##")
    formatter.roundingMode = RoundingMode.CEILING
    return formatter.format(priceValue)
  }

  private fun getPriceOnClick(isFromIncrement: Boolean) {
    var price = 0.0
    var minPrice = 0.0
    var maxPrice = 0.0

    if (isFromIncrement) {
      currentQty += 1
      txt_qty.text = currentQty.toString()
    }

    if (txt_qty.text != "1") {
      if (!isFromIncrement) {
        currentQty -= 1
        txt_qty.text = currentQty.toString()
      }

      val qty = currentQty
      if(isRequiredChecked!= null && isModifierChecked != null) {
        if (!isRequiredChecked!! && isModifierChecked!!) {
          maxPrice = tmpMaxValue!! * qty
          minPrice = tmpMinValue!! * qty

          btn_add_to_cart.text = getString(string.add_to_cart) + " " + "$$minPrice - $$maxPrice"
        } else if (isRequiredChecked!! && isModifierChecked!!) {
          if (priceProduct != null) {
            price = priceProduct!! * qty
            btn_add_to_cart.text = getString(string.add_to_cart) + " " + "$$price"
          }
        } else if (isRequiredChecked!! && !isModifierChecked!!) {
          price = priceProduct!! * qty

          btn_add_to_cart.text = getString(string.add_to_cart) + " " + "$$price"
        } else {
          btn_add_to_cart.text = getString(string.add_to_cart)
        }
      }
    }
  }

  private fun getMaxAndMinPrice(listDetailSkus: ArrayList<DetailSkus>) {
    listSkus = listDetailSkus
    val listAmount: ArrayList<Double> = ArrayList()
    var amount: Double?

    for (i in listSkus.indices) {
      amount = listSkus[i].priceSkus?.amount
      if (amount != null) {
        listAmount.add(amount)
      }
    }

    if (listAmount.size != 0) {
      maxValue = listAmount[0]
      minValue = listAmount[0]
      for (i in listAmount.indices) {
        if(maxValue!! < listAmount[i]){
          maxValue = listAmount[i]
        }
        if(minValue!! > listAmount[i]){
          minValue = listAmount[i]
        }
      }
      txt_total_price.text = "$$minValue - $$maxValue"
    }
  }

  private fun generateCartRequestModel() {
    val cartModifiersList: ArrayList<CartModifiers> = ArrayList()
    val cardRequest = CartRequest()
    val data: MutableMap<String, Any> = HashMap()
    val qty = txt_qty.text.toString()
    var cookie = ""
    var notes: String = ed_special_intruction.text.toString()

    if (Hawk.contains(Constant.REQUIRED_CHOICE_PRODUCT)) {
      val listRequiredData: ArrayList<RequiredChoiceChecked> = Hawk.get(
          Constant.REQUIRED_CHOICE_PRODUCT
      )
      if (listRequiredData.size != 0) {
        if (listRequiredData.size >= listRequiredData[0].totalRequired!!) {
          isRequiredChecked = true
          for (i in listRequiredData.indices) {
            val key : String = listRequiredData[i].category.toString()
            val value : String = listRequiredData[i].name.toString()
            data[key] = value
          }

          if (Hawk.contains(Constant.MODIFIER_CHOICE_PRODUCT)) {
            val listModifierChoiceChecked: ArrayList<ModifierChoiceChecked> = Hawk.get(
                Constant.MODIFIER_CHOICE_PRODUCT
            )
            if (listModifierChoiceChecked.size != 0) {
              isModifierChecked = true
              for (i in listModifierChoiceChecked.indices) {
                val cartModifiers = CartModifiers()
                cartModifiers.modifierGroupId = listModifierChoiceChecked[i].modifierGroupId
                cartModifiers.modifierId = listModifierChoiceChecked[i].id
                cartModifiersList.add(cartModifiers)
              }
              cardRequest.cartModifiers = cartModifiersList
            } else {
              isModifierChecked = false
            }
          } else {
            isModifierChecked = false
          }

          cardRequest.productId = productId
          cardRequest.quantity = qty.toInt()
          cardRequest.productOptions = data

          if (notes != "")  cardRequest.notes = notes

          if (Hawk.contains(Constant.KEY_CUSTOMER_ID)) {
            cookie = Hawk.get(Constant.KEY_CUSTOMER_ID)
          }
          addCartViewModel.submitDataCart(cookie, cardRequest)
        } else {
          showErrorMessage()
        }
      } else {
        showErrorMessage()
      }
    } else {
      showErrorMessage()
    }
  }

  private fun showErrorMessage() {
    isRequiredChecked = false
    Common.showMessageDialog(this, "Error", "Please complete all \"Required\" fields before adding item to cart.",
        DialogInterface.OnDismissListener { Common.dismissProgressDialog() })
  }
}