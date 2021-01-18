package com.strategies360.mililani2.fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.style
import com.strategies360.mililani2.activity.AddScanMtaCardActivity
import com.strategies360.mililani2.activity.HelpMtaActivity
import com.strategies360.mililani2.adapter.recycler.MTACardAdapter
import com.strategies360.mililani2.adapter.recycler.SingleMTACardAdapter
import com.strategies360.mililani2.adapter.viewpager.ViewPagerKotlinAdapter
import com.strategies360.mililani2.eventbus.EventDefaultCardNumber
import com.strategies360.mililani2.eventbus.EventDeleteCardNumber
import com.strategies360.mililani2.eventbus.EventPrimaryCardNumber
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.DeleteMtaCardRequest
import com.strategies360.mililani2.model.remote.mtaCard.MTACard
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.DefaultMTACardViewModel
import com.strategies360.mililani2.viewmodel.DeleteMTACardViewModel
import com.strategies360.mililani2.viewmodel.MTACardListViewModel
import com.strategies360.mililani2.viewmodel.SubmitNicknameMTACardViewModel
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.btn_add_mta_card
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.btn_close
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.btn_delete_card
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.btn_help
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.indicator
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.indicator_nol
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.layout_recycler_mta_card
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.progress_bar
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.viewPager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

class MTACardBottomListFragment : BottomSheetDialogFragment(), View.OnClickListener {

  private var adapter = MTACardAdapter()
  private var customAdapter = ViewPagerKotlinAdapter()
  private val singleAdapter = SingleMTACardAdapter()

  private val dataList = ArrayList<MTACard>()

  private var idsDeleteMtaCard: String? = null

  private val viewModel by lazy {
    ViewModelProviders.of(this)
        .get(MTACardListViewModel::class.java)
  }

  private val viewModelDeleteMTACard by lazy {
    ViewModelProviders.of(this)
        .get(DeleteMTACardViewModel::class.java)
  }

  private val setDefaultMtaCardViewModel by lazy {
    ViewModelProviders.of(this)
        .get(DefaultMTACardViewModel::class.java)
  }

  /** The view model for sign in */
  private val setNicknameMTACardViewModel by lazy {
    ViewModelProviders.of(this)
        .get(SubmitNicknameMTACardViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    setStyle(STYLE_NORMAL, style.BottomSheetDialogTheme)
    return inflater.inflate(layout.fragment_mta_card_bottom_list, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.fetchData()

    btn_add_mta_card.setOnClickListener(this)
    btn_delete_card.setOnClickListener(this)
    btn_help.setOnClickListener(this)
    btn_close.setOnClickListener(this)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViewModel()
  }

  override fun getTheme(): Int = style.BottomSheetDialogTheme

  override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
    super.onCreate(savedInstanceState)
    val view = View.inflate(context, layout.fragment_mta_card_bottom_list, null)
    val dialog = BottomSheetDialog(requireContext(), theme)

    dialog.setOnShowListener { dialogInterface ->
      val bottomSheetDialog = dialogInterface as BottomSheetDialog
      setupRatio(bottomSheetDialog)
    }
    dialog.setContentView(view)

    return dialog
  }

  private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
    val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
    val layoutParams = bottomSheet!!.layoutParams

    layoutParams.height = getBottomSheetDialogDefaultHeight()
    bottomSheet.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
  }

  private fun getBottomSheetDialogDefaultHeight(): Int {
    return getWindowHeight() * 85 / 100
  }

  private fun getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
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

      if (it.size == 0) {
        btn_delete_card.visibility = View.GONE
      } else {
        btn_delete_card.visibility = View.VISIBLE
        dataList.addAll(it)
        if (it.size > 1) {
          adapter.setDataList(it)
          initRecyclerCategory(false)
        } else {
          singleAdapter.setDataList(it)
          initRecyclerCategory(true)
        }
      }
    })

    viewModelDeleteMTACard.deleteLiveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onDeleteMTACardLoading()
        Resource.SUCCESS -> onDeleteMTACardSuccess()
        Resource.ERROR -> onDeleteMTACardFailure()
      }
    })

    setDefaultMtaCardViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onDefaultMTACardLoading()
        Resource.ERROR -> onDefaultMTACardFailure()
        Resource.SUCCESS -> onDefaultMTACardSuccess()
      }
    })

    setNicknameMTACardViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onSetNicknameMTACardLoading()
        Resource.ERROR -> onSetNicknameMTACardFailure()
        Resource.SUCCESS -> onSetNicknameMTACardSuccess()
      }
    })
    lifecycle.addObserver(viewModel)
  }

  private fun initRecyclerCategory(isSingle: Boolean) {
    customAdapter.viewPagerKotlinAdapter(dataList, requireContext())
    viewPager.adapter = customAdapter
    viewPager.setPadding(130, 0, 130, 0)

    if (isSingle) {
      indicator_nol.visibility = View.VISIBLE
      indicator.visibility = View.GONE
    } else {
      indicator_nol.visibility = View.GONE
      indicator.visibility = View.VISIBLE

      indicator.attachToPager(viewPager)
      indicator.setDotCount(dataList.size)
    }
  }

  private fun onMTACardListLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_recycler_mta_card.visibility = View.INVISIBLE
  }

  private fun onMTACardListSuccess() {
    progress_bar.visibility = View.GONE
    if (dataList.size == 0) {
      AddScanMtaCardActivity.launchIntent(requireContext(), true)
    } else if (dataList.size == 1 && dataList[0].isDefault == 0) {
      setDefaultMtaCardViewModel.defaultMtaCard(dataList[0].cardNumber.toString())
    } else {
      layout_recycler_mta_card.visibility = View.VISIBLE
    }
  }

  private fun onMTACardListFailure() {
    progress_bar.visibility = View.GONE
    layout_recycler_mta_card.visibility = View.VISIBLE
    btn_delete_card.visibility = View.GONE
  }

  private fun onDeleteMTACardLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_recycler_mta_card.visibility = View.INVISIBLE
  }

  private fun onDeleteMTACardSuccess() {
    viewModel.fetchData()
    progress_bar.visibility = View.GONE
    if (dataList.size == 0) {
      AddScanMtaCardActivity.launchIntent(requireContext(), true)
    } else {
      layout_recycler_mta_card.visibility = View.VISIBLE
    }
  }

  private fun onDeleteMTACardFailure() {
    progress_bar.visibility = View.GONE
    layout_recycler_mta_card.visibility = View.VISIBLE
  }

  private fun onDefaultMTACardLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_recycler_mta_card.visibility = View.INVISIBLE
  }

  private fun onDefaultMTACardSuccess() {
    progress_bar.visibility = View.GONE
    layout_recycler_mta_card.visibility = View.VISIBLE
    viewModel.fetchData()
  }

  private fun onDefaultMTACardFailure() {
    progress_bar.visibility = View.GONE
    layout_recycler_mta_card.visibility = View.VISIBLE
  }

  private fun onSetNicknameMTACardLoading() {}

  private fun onSetNicknameMTACardSuccess() {
    viewModel.fetchData()
  }

  private fun onSetNicknameMTACardFailure() {}

  override fun onClick(view: View?) {
    if (view == btn_add_mta_card) {
      AddScanMtaCardActivity.launchIntent(requireContext())
    } else if (view == btn_delete_card) {
      if (dataList.size != 0) {
        viewPager.currentItem
        idsDeleteMtaCard = dataList[viewPager.currentItem].id.toString()
        val deleteMtaCardRequest = DeleteMtaCardRequest()
        val idMtaCard = ArrayList<String>()
        idMtaCard.add(idsDeleteMtaCard!!)
        deleteMtaCardRequest.idsList = idMtaCard

        Hawk.put((Constant.KEY_REQUEST_DELETE_CARD_NUMBER), deleteMtaCardRequest)
        DialogConfirmationDeleteCard().show(childFragmentManager, "dialog")
        idsDeleteMtaCard = ""
      } else {
        btn_add_mta_card.isEnabled = false
      }
    } else if (view == btn_help) {
      HelpMtaActivity.launchIntent(requireContext())
    } else if (view == btn_close) {
      dismiss()
    }
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onSetDefaultCardNumber(event: EventDefaultCardNumber) {
    setDefaultMtaCardViewModel.defaultMtaCard(event.carNumberEvent)
    EventBus.getDefault().removeStickyEvent(event)
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onDeleteCardNumber(event: EventDeleteCardNumber) {
    adapter.notifyDataSetChanged()
    viewModel.fetchData()
    EventBus.getDefault().removeStickyEvent(event)
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onPrimaryCardNumber(event: EventPrimaryCardNumber) {
    adapter.notifyDataSetChanged()
    val mtaCardRequest = MTACardRequest()
    mtaCardRequest.nickname = event.primaryNicknameEvent
    setNicknameMTACardViewModel.editNicknameMtaCard(event.primaryCardNumberEvent, mtaCardRequest)
    EventBus.getDefault().removeStickyEvent(event)
  }
}