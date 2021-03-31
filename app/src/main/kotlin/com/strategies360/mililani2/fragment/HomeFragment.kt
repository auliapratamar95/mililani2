package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.ProfileMtaActivity
import com.strategies360.mililani2.adapter.viewpager.ViewPagerNewsAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.fragment_home.btn_logout
import kotlinx.android.synthetic.main.fragment_home.btn_open_profile
import kotlinx.android.synthetic.main.fragment_home.btn_scan_barcode
import kotlinx.android.synthetic.main.fragment_home.newsViewPager

class HomeFragment : CoreFragment(), View.OnClickListener {

  private var isBottomCardList = false
  private var customAdapter = ViewPagerNewsAdapter()

  private val dataList = ArrayList<News>()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(NewsListViewModel::class.java)
  }

  override val viewRes: Int = layout.fragment_home

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initView()
  }

  private fun initView() {
    initViewModel()
    viewModel.fetchData()

    Hawk.put((Constant.FLAG_ON_BACK_MENU), true)
    isBottomCardList = (requireActivity().intent.getBooleanExtra(
        getString(
            string.prefs_is_bottom_card_list
        ), false
    ))

    if (isBottomCardList) openBottomCardList()

    btn_logout.visibility = View.GONE
    newsViewPager.visibility = View.VISIBLE
    btn_logout.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
    btn_open_profile.setOnClickListener(this)
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }

  private fun openDialogSignout() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      DialogConfirmationLogout()
          .show(fragManager, "Dialog")
    }
  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onMTACardListLoading()
        Resource.SUCCESS -> onMTACardListSuccess()
        Resource.ERROR -> onMTACardListFailure()
      }
    })

    viewModel.dataList.observe(viewLifecycleOwner, Observer {
      dataList.clear()
      dataList.addAll(it)
      initRecyclerCategory()
    })
    lifecycle.addObserver(viewModel)
  }

  private fun onMTACardListLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        viewModel.cancel()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onMTACardListSuccess() {
    Common.dismissProgressDialog()
  }

  private fun onMTACardListFailure() {
    Common.dismissProgressDialog()
  }

  private fun initRecyclerCategory() {
    customAdapter.viewPagerKotlinAdapter(dataList, requireContext())
    newsViewPager.adapter = customAdapter
    newsViewPager.setPadding(50, 0, 50, 0)
  }

  private fun openProfile() {
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
    ProfileMtaActivity.launchIntent(requireContext())
  }

  override fun onClick(view: View?) {
    when (view) {
      btn_logout -> {
        openDialogSignout()
      }
      btn_scan_barcode -> {
        openBottomCardList()
      }
      btn_open_profile -> {
        openProfile()
      }
    }
  }
}