package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.EventsResultActivity
import com.strategies360.mililani2.activity.ProfileMtaActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.tickets.EventsResponse
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.EventsListViewModel
import com.strategies360.mililani2.widget.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_events.btn_open_profile
import kotlinx.android.synthetic.main.fragment_events.btn_reset
import kotlinx.android.synthetic.main.fragment_events.btn_scan_barcode
import kotlinx.android.synthetic.main.fragment_events.btn_search
import kotlinx.android.synthetic.main.fragment_events.ed_end_date
import kotlinx.android.synthetic.main.fragment_events.ed_start_date
import kotlinx.android.synthetic.main.fragment_events.edit_keyword_search
import kotlinx.android.synthetic.main.fragment_events.edit_ticket_code
import java.util.Calendar

class TicketsFragment : CoreFragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener  {

  private var mSelectedDay = -1
  private var mSelectedMonth = -1
  private var mSelectedYear = -1

  private var startDate: String = ""
  private var endDate: String = ""

  private var isDateFilter = false

  private var ticketCode: String = ""
  private var beginEventDate: String = ""
  private var endEventDate: String = ""
  private var key: String = ""

  /** The view model for sign in */
  private val eventsListViewModel by lazy {
    ViewModelProviders.of(this)
        .get(EventsListViewModel::class.java)
  }

  override val viewRes: Int
    get() = R.layout.fragment_events

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    btn_search.setOnClickListener(this)
    btn_open_profile.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
    ed_start_date.setOnClickListener(this)
    ed_end_date.setOnClickListener(this)
    btn_reset.setOnClickListener(this)

    ed_start_date.keyListener = null
    ed_end_date.keyListener = null

    ed_start_date.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        showDatePicker(true)
      }
    }

    ed_end_date.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        showDatePicker(false)
      }
    }

    initViewModel()
  }

  private fun initViewModel() {
    eventsListViewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onEventsLoading()
        Resource.ERROR -> onEventsFailure(it.error)
        Resource.SUCCESS -> onEventsSuccess(it.data!!)
      }
    })
  }

  /** Load user's profile from a remote server (async)  */
  private fun getEvents() {
    if (edit_ticket_code.text.isNotEmpty()) ticketCode = edit_ticket_code.text.toString()
    if (edit_keyword_search.text.isNotEmpty()) key = edit_keyword_search.text.toString()

    eventsListViewModel.fetchFromRemote(ticketCode, beginEventDate, endEventDate, key)
  }

  private fun onEventsLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        eventsListViewModel.cancel()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onEventsSuccess(response: EventsResponse) {
    Common.dismissProgressDialog()
    if (response.eventsList != null) {
      Hawk.put((Constant.KEY_TICKET_LIST), response.eventsList)
      EventsResultActivity.launchIntent(requireContext())
    }
  }

  private fun onEventsFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      Common.showMessageDialog(it, "Error", error.message)
    }
  }

  private fun openBottomFilter() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      CustomFilterBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }

  private fun openProfile() {
    Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
    ProfileMtaActivity.launchIntent(requireContext())
  }


  /**
   * Show a date picker for date-of-birth.
   */
  private fun showDatePicker(isFilter: Boolean) {
    isDateFilter = isFilter

    val calendar = Calendar.getInstance()
    calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = calendar[Calendar.DAY_OF_MONTH]
    val initYear: Int
    val initMonth: Int
    val initDay: Int
    if (mSelectedDay == -1) {
      initYear = calendar[Calendar.YEAR]
      initMonth = calendar[Calendar.MONTH]
      initDay = calendar[Calendar.DAY_OF_MONTH]
    } else {
      initYear = mSelectedYear
      initMonth = mSelectedMonth - 1
      initDay = mSelectedDay
    }
    DatePickerDialog.Builder(context)
        .withMaxDate(calendar.timeInMillis)
        .withInitialSelection(initYear, initMonth, initDay)
        .withCalendarView(true)
        .withOnDateSetListener(this)
        .build().show()
  }

  override fun onDateSet(
    dialog: DatePickerDialog?,
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int
  ) {
    val month: String
    val day: String

    mSelectedDay = dayOfMonth
    mSelectedMonth = monthOfYear + 1
    mSelectedYear = year

    val convertMonth: String = mSelectedMonth.toString()
    val convertYear: String = mSelectedYear.toString()
    val convertDay: String = mSelectedDay.toString()
    if (convertDay.length == 1) {
      day = "0$convertDay"
      mSelectedDay = day.toInt()
    } else {
      day = mSelectedDay.toString()
    }

    if (convertMonth.length == 1) {
      month = "0$convertMonth"
      mSelectedMonth = month.toInt()
    } else {
      month = mSelectedMonth.toString()
    }

    if (isDateFilter) {
      ed_start_date?.error = null
      ed_start_date.setText(
          String.format(
              resources.getString(R.string.date_filter_dd_mm_yyyy), mSelectedDay, mSelectedMonth,
              mSelectedYear
          )
      )
      startDate = String.format(
          resources.getString(R.string.date_filter_dd_mm_yyyy), mSelectedYear, mSelectedMonth,
          mSelectedDay
      )

      beginEventDate = "$convertYear-$month-$day"
      // Automatically requests focus to the address EditText
      ed_end_date?.requestFocus()
    } else {
      ed_end_date?.error = null
      ed_end_date?.setText(
          String.format(
              resources.getString(R.string.date_filter_dd_mm_yyyy), mSelectedDay, mSelectedMonth,
              mSelectedYear
          )
      )
      endDate = String.format(
          resources.getString(R.string.date_filter_dd_mm_yyyy), mSelectedYear, mSelectedMonth,
          mSelectedDay
      )
      endEventDate = "$convertYear-$month-$day"
    }
  }

  override fun onCancelled(dialog: DatePickerDialog?) {}

  private fun clearDataSearch() {
    ed_start_date.setText("")
    ed_end_date.setText("")
    edit_ticket_code.setText("")
    edit_keyword_search.setText("")
  }

  override fun onClick(v: View?) {
    when (v) {
      btn_search -> {
        getEvents()
      }
      btn_open_profile -> {
        openProfile()
      }
      btn_scan_barcode -> {
        openBottomCardList()
      }
      ed_start_date -> {
        showDatePicker(true)
      }
      ed_end_date -> {
        showDatePicker(false)
      }
      btn_reset -> {
        clearDataSearch()
      }
    }
  }
}