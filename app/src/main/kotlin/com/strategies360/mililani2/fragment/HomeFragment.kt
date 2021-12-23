package com.strategies360.mililani2.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.*
import com.strategies360.mililani2.adapter.viewpager.ViewPagerNewsAdapter
import com.strategies360.mililani2.eventbus.EventChangeQuickLink
import com.strategies360.mililani2.eventbus.EventFilterResult
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.local.categoryquicklink.CategoryQuickLink
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.model.remote.reservation.facilitySchedule.FacilityScheduleFile
import com.strategies360.mililani2.model.remote.reservation.facilitySchedule.FacilityScheduleList
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.FacilityScheduleListViewModel
import com.strategies360.mililani2.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_menu_quick_link.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : CoreFragment(), View.OnClickListener {

  private var isBottomCardList = false
  private var customAdapter = ViewPagerNewsAdapter()

  private val dataList = ArrayList<News>()
  private var tempListDataQuickLink = ArrayList<CategoryQuickLink>()
  private val dataFacilityScheduleList = ArrayList<FacilityScheduleFile>()

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(NewsListViewModel::class.java)
  }

  private val viewModelFacilitySchedule by lazy {
    ViewModelProviders.of(this)
      .get(FacilityScheduleListViewModel::class.java)
  }

  override val viewRes: Int = layout.fragment_home

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initView()
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
    initViewModel()
    initViewModelFacilitySchedule()
    viewModel.fetchData()

    Hawk.put((Constant.FLAG_ON_BACK_MENU), true)
    isBottomCardList = (requireActivity().intent.getBooleanExtra(
        getString(
            string.prefs_is_bottom_card_list
        ), false
    ))

    if (isBottomCardList) openBottomCardList()

    getCategoryQuickLink()

    btn_logout.visibility = View.GONE
    newsViewPager.visibility = View.VISIBLE
    btn_logout.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
    btn_open_profile.setOnClickListener(this)
    btn_edit_quick_link.setOnClickListener(this)
    card_contact_us.setOnClickListener(this)

    bg_dash_one.setOnClickListener(this)
    bg_dash_two.setOnClickListener(this)
    bg_dash_three.setOnClickListener(this)
    bg_dash_four.setOnClickListener(this)

    btn_open_news.setOnClickListener(this)
    btn_pool_schedule.setOnClickListener(this)
//    img_quick_link_two.setOnClickListener(this)
//    img_quick_link_three.setOnClickListener(this)
//    img_quick_link_four.setOnClickListener(this)
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

  private fun openSetting() {
    BottomMenuNavigationActivity.launchIntent(requireContext(), "setting")
  }

  private fun openEditQuickLink() {
    SettingMtaActivity.launchIntent(requireContext())
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
      Hawk.put((Constant.KEY_LIST_NEWS), dataList)
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
//    Common.dismissProgressDialog()
    viewModelFacilitySchedule.fetchFromRemote()
  }

  private fun onMTACardListFailure() {
    Common.dismissProgressDialog()
  }

  private fun initRecyclerCategory() {
    customAdapter.viewPagerKotlinAdapter(dataList, requireContext())
    newsViewPager.adapter = customAdapter
    newsViewPager.setPadding(50, 0, 100, 0)
  }

  private fun openProfile() {
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
    ProfileMtaActivity.launchIntent(requireContext())
  }

  private fun showHideIconQuickLink() {
    if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
      val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
      if (listCategoryQuickLink.size != 0) {
        btn_edit_quick_link.text = "Save"
        when (listCategoryQuickLink.size) {
          0 -> {
            showIconQuickLink()
          }
          1 -> {
            btn_delete_quick_link_one.visibility = View.VISIBLE
          }
          2 -> {
            btn_delete_quick_link_one.visibility = View.VISIBLE
            btn_delete_quick_link_two.visibility = View.VISIBLE
          }
          3 -> {
            btn_delete_quick_link_one.visibility = View.VISIBLE
            btn_delete_quick_link_two.visibility = View.VISIBLE
            btn_delete_quick_link_three.visibility = View.VISIBLE
          }
          4 -> {
            btn_delete_quick_link_one.visibility = View.VISIBLE
            btn_delete_quick_link_two.visibility = View.VISIBLE
            btn_delete_quick_link_three.visibility = View.VISIBLE
            btn_delete_quick_link_four.visibility = View.VISIBLE
          }
        }
      }
    } else {
      hideIconQuickLink()
    }
  }

  private fun showIconQuickLink() {
    btn_delete_quick_link_one.visibility = View.VISIBLE
    btn_delete_quick_link_two.visibility = View.VISIBLE
    btn_delete_quick_link_three.visibility = View.VISIBLE
    btn_delete_quick_link_four.visibility = View.VISIBLE
  }

  private fun hideIconQuickLink() {
//    btn_edit_quick_link.text = "Edit"

    btn_delete_quick_link_one.visibility = View.GONE
    btn_delete_quick_link_two.visibility = View.GONE
    btn_delete_quick_link_three.visibility = View.GONE
    btn_delete_quick_link_four.visibility = View.GONE
  }

  private fun hideIconQuickLink(index: Int) {
    when (index) {
        1 -> {
          btn_delete_quick_link_one.visibility = View.GONE
        }
        2 -> {
          btn_delete_quick_link_two.visibility = View.GONE
        }
        3 -> {
          btn_delete_quick_link_three.visibility = View.GONE
        }
        else -> {
          btn_delete_quick_link_four.visibility = View.GONE
        }
    }
  }

  private fun getCategoryQuickLink() {
    if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
      val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
      tempListDataQuickLink.clear()
      tempListDataQuickLink.addAll(listCategoryQuickLink)
      if (tempListDataQuickLink.size < 4 || tempListDataQuickLink.size == 4) {
        hideIconQuickLinkDash()
        hideIconQuickLink()
        for (i in tempListDataQuickLink.indices) {
          when (i + 1) {
            1 -> {
              var isDeleteQuickCategoryOne = false
              if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickCategoryOne = true

              bg_dash_one.visibility = View.GONE
              txt_quick_link_one.visibility = View.VISIBLE
              showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_one, txt_quick_link_one, 1,  isDeleteQuickCategoryOne)
            }
            2 -> {
              var isDeleteQuickCategoryTwo = false
              if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickCategoryTwo = true
              bg_dash_two.visibility = View.GONE
              txt_quick_link_one.visibility = View.VISIBLE
              showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_two, txt_quick_link_two,2, isDeleteQuickCategoryTwo)
            }
            3 -> {
              var isDeleteQuickCategoryThree = false
              if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickCategoryThree = true
              bg_dash_three.visibility = View.GONE
              txt_quick_link_one.visibility = View.VISIBLE
              showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_three, txt_quick_link_three, 3, isDeleteQuickCategoryThree)
            }
            4 -> {
              var isDeleteQuickCategoryFour = false
              if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickCategoryFour = true
              bg_dash_four.visibility = View.GONE
              txt_quick_link_one.visibility = View.VISIBLE
              showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_four, txt_quick_link_four, 4, isDeleteQuickCategoryFour)
            }
          }
        }
      } else {
        showIconQuickLinkDash()
      }
    }
    else {
      for (i in 0..3) {
        when (i + 1) {
          1 -> {
            var isDeleteQuickCategoryOne = false
            if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickCategoryOne = true

            bg_dash_one.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showIconQuickLinkOne(1, img_quick_link_one, txt_quick_link_one, 1,  isDeleteQuickCategoryOne)
            saveCategoryQuickLink(1, "Caffe")
          }
          2 -> {
            var isDeleteQuickCategoryTwo = false
            if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickCategoryTwo = true
            bg_dash_three.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showIconQuickLinkOne(4, img_quick_link_three, txt_quick_link_three, 2, isDeleteQuickCategoryTwo)
            saveCategoryQuickLink(4, "Pay Asses..")
          }
          3 -> {
            var isDeleteQuickCategoryThree = false
            if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickCategoryThree = true
            bg_dash_two.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showIconQuickLinkOne(8, img_quick_link_two, txt_quick_link_two,3, isDeleteQuickCategoryThree)
            saveCategoryQuickLink(8, "Rec. Centers")
          }
          4 -> {
            var isDeleteQuickCategoryFour = false
            if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickCategoryFour = true
            bg_dash_four.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showIconQuickLinkOne(3, img_quick_link_four, txt_quick_link_four, 4, isDeleteQuickCategoryFour)
            saveCategoryQuickLink(3, "Employm..")
          }
        }
      }

    }
  }

  private fun saveCategoryQuickLink(id: Int, name: String) {
    var isCategoryAvailable = false
    if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
      val saveCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
      val getCategoryQuickLink: ArrayList<CategoryQuickLink> =
        Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)

      if (getCategoryQuickLink.size < 4) {
        for (i in getCategoryQuickLink.indices) {
          if (id == getCategoryQuickLink[i].id) {
            isCategoryAvailable = true
            break
          }
        }

        if (!isCategoryAvailable) {
          val categoryQuickLink = CategoryQuickLink()
          categoryQuickLink.id = id
          categoryQuickLink.name = name
          getCategoryQuickLink.add(categoryQuickLink)
        }
        saveCategoryQuickLink.addAll(getCategoryQuickLink)
        Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), saveCategoryQuickLink)
        tempListDataQuickLink.addAll(saveCategoryQuickLink)
      }
    } else {
      val listCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
      val categoryQuickLink = CategoryQuickLink()
      categoryQuickLink.id = id
      categoryQuickLink.name = name
      listCategoryQuickLink.add(categoryQuickLink)
      Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), listCategoryQuickLink)
      tempListDataQuickLink.add(categoryQuickLink)
    }
  }

  private fun deleteQuickLink(id: Int) {
    if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
      val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
      val tmpListQuickLink : ArrayList<CategoryQuickLink> = ArrayList()

      for (i in listCategoryQuickLink.indices) {
        if (listCategoryQuickLink[i].id != id) {
          tmpListQuickLink.add(listCategoryQuickLink[i])
        }
      }
      Hawk.delete(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
      Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), tmpListQuickLink)
      getCategoryQuickLink()
    }
  }

  private fun deleteTempQuickLink(id: Int, index: Int) {
    if (tempListDataQuickLink.size != 0) {
      val tmpListQuickLink : ArrayList<CategoryQuickLink> = ArrayList()

      for (i in tempListDataQuickLink.indices) {
        if (tempListDataQuickLink[i].id != id) {
          tmpListQuickLink.add(tempListDataQuickLink[i])
        }
      }
      hideIconQuickLink(index)
      tempListDataQuickLink.clear()
      tempListDataQuickLink.addAll(tmpListQuickLink)
      getTempCategoryQuickLink()
    }
  }

  private fun showIconQuickLinkOne(id: Int?, image: ImageView, textView: TextView, index: Int, isQuickCategoryDelete: Boolean) {
    image.visibility = View.VISIBLE
    textView.visibility = View.VISIBLE
    when (id) {
      1 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            CaffeActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_caffe)
        textView.text = resources.getText(string.cafe)
      }
      2 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            HelpMtaActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_household)
        textView.text = "Househo.."
      }
      3 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            EmploymentActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_employment)
        textView.text = "Employm.."
      }
      4 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            WebviewAssessmentActivity.launchIntent(requireContext(), "https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=PSS")
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_pay)
        textView.text = "Pay Asses.."
      }
      5 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            deleteQuickLink(id)
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_form)
        textView.text = resources.getText(R.string.forms)
      }
      6 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            NewsActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_news)
        textView.text = "News Lett.."
      }
      7 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            NotificationActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_notification)
        textView.text = "Notificati.."
      }
      8 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            RecCenterActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_rec_center)
        textView.text = "Rec. Centers"
      }
      9 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            RecCenterActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_waitlist)
        textView.text = "Waitlist.."
      }
      10 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteQuickLink(id)
          } else {
            WebviewHistoryActivity.launchIntent(requireContext())
          }
        }
        textView.text = resources.getText(string.history)
        image.setImageResource(R.drawable.ic_quick_link_history)
      }
    }
  }

  private fun showTempIconQuickLinkOne(id: Int?, image: ImageView, textView: TextView, index: Int, isQuickCategoryDelete: Boolean) {
    image.visibility = View.VISIBLE
    textView.visibility = View.VISIBLE
    when (id) {
      1 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            CaffeActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_caffe)
        textView.text = resources.getText(string.cafe)
      }
      2 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            HelpMtaActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_household)
        textView.text = "Househo.."
      }
      3 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            EmploymentActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_employment)
        textView.text = "Employm.."
      }
      4 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            WebviewAssessmentActivity.launchIntent(requireContext(), "https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=PSS")
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_pay)
        textView.text = "Pay Asses.."
      }
      5 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            deleteTempQuickLink(id, index)
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_form)
        textView.text = resources.getText(R.string.forms)
      }
      6 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            NewsActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_news)
        textView.text = "News Lett.."
      }
      7 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            NotificationActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_notification)
        textView.text = "Notificati.."
      }
      8 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            RecCenterActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_rec_center)
        textView.text = "Rec. Centers"
      }
      9 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            RecCenterActivity.launchIntent(requireContext())
          }
        }
        image.setImageResource(R.drawable.ic_quick_link_waitlist)
        textView.text = "Waitlist.."
      }
      10 -> {
        image.setOnClickListener {
          if (getCaffe(index)) {
            deleteTempQuickLink(id, index)
          } else {
            WebviewHistoryActivity.launchIntent(requireContext())
          }
        }
        textView.text = resources.getText(string.history)
        image.setImageResource(R.drawable.ic_quick_link_history)
      }
    }
  }

  private fun showIconQuickLinkDash() {
    txt_quick_link_one.visibility = View.INVISIBLE
    txt_quick_link_two.visibility = View.INVISIBLE
    txt_quick_link_three.visibility = View.INVISIBLE
    txt_quick_link_four.visibility = View.INVISIBLE

    bg_dash_one.visibility = View.VISIBLE
    bg_dash_two.visibility = View.VISIBLE
    bg_dash_three.visibility = View.VISIBLE
    bg_dash_four.visibility = View.VISIBLE
  }

  private fun hideIconQuickLinkDash() {
    txt_quick_link_one.visibility = View.INVISIBLE
    txt_quick_link_two.visibility = View.INVISIBLE
    txt_quick_link_three.visibility = View.INVISIBLE
    txt_quick_link_four.visibility = View.INVISIBLE

    img_quick_link_one.visibility = View.GONE
    img_quick_link_two.visibility = View.GONE
    img_quick_link_four.visibility = View.GONE
    img_quick_link_three.visibility = View.GONE

    bg_dash_one.visibility = View.VISIBLE
    bg_dash_two.visibility = View.VISIBLE
    bg_dash_three.visibility = View.VISIBLE
    bg_dash_four.visibility = View.VISIBLE
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
      btn_edit_quick_link -> {
        if (btn_edit_quick_link.text.equals("Save")) {
          deleteDataQuickLink()
        } else {
          showHideIconQuickLink()
        }
      }
      bg_dash_one -> {
        openEditQuickLink()
      }
      bg_dash_two -> {
        openEditQuickLink()
      }
      bg_dash_three -> {
        openEditQuickLink()
      }
      bg_dash_four -> {
        openEditQuickLink()
      }
      btn_open_news -> {
        NewsActivity.launchIntent(requireContext())
      }

      btn_pool_schedule -> {
        if (dataFacilityScheduleList.size != 0) {
          if (dataFacilityScheduleList[0].facilityScheduleFile?.size  != 0) {
            Hawk.put((Constant.KEY_POOL_SCHEDULE), dataFacilityScheduleList[0].facilityScheduleFile?.last()?.facilitySchedule?.url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dataFacilityScheduleList[0].facilityScheduleFile?.last()?.facilitySchedule?.url))
            startActivity(browserIntent)
          }
        }
      }

      card_contact_us -> {
        ContactUSActivity.launchIntent(requireContext())
      }
    }
  }

  private fun getCaffe(index: Int): Boolean {
    var isDeleteQuickLink = false

    if (index == 1) {
      if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickLink = true
    } else if (index == 2) {
      if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickLink = true
    } else if (index == 3) {
      if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickLink = true
    } else if (index == 4) {
      if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickLink = true
    }
    return isDeleteQuickLink
  }


  private fun initViewModelFacilitySchedule() {
    viewModelFacilitySchedule.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onEventsResultLoading()
        Resource.ERROR -> onEventsResultFailure(it.error)
        Resource.SUCCESS -> onEventsResultSuccess(it.data)
      }
    })

    viewModelFacilitySchedule.dataList.observe(viewLifecycleOwner, Observer {
      dataFacilityScheduleList.clear()
      dataFacilityScheduleList.addAll(it)

      if (dataFacilityScheduleList.size != 0) {
        if (dataFacilityScheduleList[0].facilityScheduleFile?.size  != 0) {
          Hawk.put((Constant.KEY_POOL_SCHEDULE), dataFacilityScheduleList[0].facilityScheduleFile?.last()?.facilitySchedule?.url)
        }
      }
    })
    lifecycle.addObserver(viewModel)
  }

  private fun onEventsResultLoading() {}

  private fun onEventsResultSuccess(facilityScheduleResponse: FacilityScheduleList?) {
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

  private fun getTempCategoryQuickLink() {
    if (tempListDataQuickLink.size < 4 || tempListDataQuickLink.size == 4) {
      hideIconQuickLinkDash()
      for (i in tempListDataQuickLink.indices) {
        when (i + 1) {
          1 -> {
            var isDeleteQuickCategoryOne = false
            if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickCategoryOne = true

            bg_dash_one.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_one, txt_quick_link_one, 1,  isDeleteQuickCategoryOne)
          }
          2 -> {
            var isDeleteQuickCategoryTwo = false
            if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickCategoryTwo = true
            bg_dash_two.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_two, txt_quick_link_two,2, isDeleteQuickCategoryTwo)
          }
          3 -> {
            var isDeleteQuickCategoryThree = false
            if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickCategoryThree = true
            bg_dash_three.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_three, txt_quick_link_three, 3, isDeleteQuickCategoryThree)
          }
          4 -> {
            var isDeleteQuickCategoryFour = false
            if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickCategoryFour = true
            bg_dash_four.visibility = View.GONE
            txt_quick_link_one.visibility = View.VISIBLE
            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_four, txt_quick_link_four, 4, isDeleteQuickCategoryFour)
          }
        }
      }
    } else {
      showIconQuickLinkDash()
    }
  }

  private fun deleteDataQuickLink() {
    Hawk.delete(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
    Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), tempListDataQuickLink)
    hideIconQuickLink()
    btn_edit_quick_link.text = "Edit"
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  fun onChangeQuickLink(event: EventChangeQuickLink) {
    getCategoryQuickLink()
    EventBus.getDefault().removeStickyEvent(event)
  }
}