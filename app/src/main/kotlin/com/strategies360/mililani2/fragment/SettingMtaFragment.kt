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
import com.strategies360.mililani2.model.remote.caffe.CaffeListResponse
import com.strategies360.mililani2.model.remote.caffe.CategoryListResponse
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import com.strategies360.mililani2.viewmodel.CategoryListViewModel
import kotlinx.android.synthetic.main.fragment_setting_mta_card.*
import kotlinx.android.synthetic.main.include_toolbar.*

class SettingMtaFragment: CoreFragment() {
  private val viewModel by lazy {
    ViewModelProviders.of(this)
            .get(CategoryListViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_setting_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)

    initView()
    viewModel.fetchCategoryFromRemote()

    btn_barcode.setOnClickListener {
      openBottomCardList()
    }

    btn_back.setOnClickListener{
      BottomMenuNavigationActivity.launchIntent(requireContext())
    }

    btn_caffe.setOnClickListener {
      CaffeActivity.launchIntent(requireContext())
    }
  }

  private fun initView() {
    initViewModel()
  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onCategoryLoading()
        Resource.ERROR -> onCategoryFailure()
        Resource.SUCCESS -> onCategorySuccess(it.data!!)
      }
    })
  }

  private fun onCategoryLoading() {
    progress_setting.visibility = View.VISIBLE
  }

  private fun onCategorySuccess(categoryList: CategoryListResponse) {
    Hawk.put((Constant.FLAG_ON_CATEGORY), categoryList.categoryListResponse)
    progress_setting.visibility = View.GONE
    layout_setting.visibility = View.VISIBLE
  }

  private fun onCategoryFailure() {
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