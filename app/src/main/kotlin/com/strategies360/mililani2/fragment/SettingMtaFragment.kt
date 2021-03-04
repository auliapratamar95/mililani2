package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.activity.CaffeActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeResponse
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import kotlinx.android.synthetic.main.fragment_setting_mta_card.btn_caffe
import kotlinx.android.synthetic.main.fragment_setting_mta_card.layout_setting
import kotlinx.android.synthetic.main.fragment_setting_mta_card.progress_setting
import kotlinx.android.synthetic.main.include_toolbar.btn_back
import kotlinx.android.synthetic.main.include_toolbar.btn_barcode

class SettingMtaFragment: CoreFragment() {
//  private val viewModel by lazy {
//    ViewModelProviders.of(this)
//            .get(CategoryListViewModel::class.java)
//  }

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(CaffeListViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_setting_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)

    initView()
    viewModel.fetchProductCaffe()

    btn_barcode.setOnClickListener {
      openBottomCardList()
    }

    btn_back.setOnClickListener{
      BottomMenuNavigationActivity.launchIntent(requireContext())
    }

    btn_caffe.setOnClickListener {
      Hawk.delete(Constant.REQUIRED_CHOICE_PRODUCT)
      CaffeActivity.launchIntent(requireContext())
    }
  }

  private fun initView() {
    initViewModel()
  }

  private fun initViewModel() {
    viewModel.resourceProductCaffe.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onProductCaffeLoading()
        Resource.ERROR -> onProductCaffeFailure()
        Resource.SUCCESS -> onProductCaffeSuccess(it.data!!)
      }
    })
  }

  private fun onProductCaffeLoading() {
    progress_setting.visibility = View.VISIBLE
  }

  private fun onProductCaffeSuccess(productCaffeResponse: ProductCaffeResponse) {
    Hawk.delete(Constant.PRODUCT_CAFFE_LIST)
    Hawk.delete(Constant.KEY_ID_CATEGORY)

    Hawk.put((Constant.PRODUCT_CAFFE_LIST), productCaffeResponse.caffeListResponse)
    progress_setting.visibility = View.GONE
    layout_setting.visibility = View.VISIBLE
  }

  private fun onProductCaffeFailure() {
    layout_setting.visibility = View.VISIBLE
    progress_setting.visibility = View.GONE
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }
}