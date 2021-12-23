package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.ProfileMtaActivity
import com.strategies360.mililani2.activity.ReservationActivity
import com.strategies360.mililani2.activity.SelectFacilityActivity
import com.strategies360.mililani2.activity.SelectLocationActivity
import com.strategies360.mililani2.adapter.recycler.SelectFacilityAdapter
import com.strategies360.mililani2.adapter.recycler.SelectLocationAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.reservation.Reservation
import com.strategies360.mililani2.model.remote.reservation.ReservationsResponse
import com.strategies360.mililani2.model.remote.reservation.SelectFacility
import com.strategies360.mililani2.model.remote.reservation.SelectLocation
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.ReservationsListViewModel
import com.strategies360.mililani2.widget.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_facility_reservation.*
import java.util.*
import kotlin.collections.ArrayList


class ReservationsFragment : CoreFragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener  {

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

  var mLayoutManager: RecyclerView.LayoutManager? = null

  /** The view model for sign in */
  private val reservationsListViewModel by lazy {
    ViewModelProviders.of(this)
        .get(ReservationsListViewModel::class.java)
  }

  override val viewRes: Int
    get() = R.layout.fragment_facility_reservation

  override fun onViewCreated(
          view: View,
          savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    deleteHawksReservations()

    btn_search.setOnClickListener(this)
    btn_open_profile.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
    ed_start_date.setOnClickListener(this)
    ed_end_date.setOnClickListener(this)
    btn_reset.setOnClickListener(this)
    btn_select_facility.setOnClickListener(this)
    btn_select_location.setOnClickListener(this)
    recycler_select_location.setOnClickListener(this)
    recycler_select_facility_reservation.setOnClickListener(this)

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

  override fun onResume() {
    super.onResume()
    if (Hawk.contains(Constant.KEY_SELECT_LOCATION_LIST)) {
      initRecyclerLocation()
    }

    if (Hawk.contains(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)) {
      initRecyclerFacility()
    }
  }

  private fun initViewModel() {
    reservationsListViewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onReservationLoading()
        Resource.ERROR -> onReservationFailure(it.error)
        Resource.SUCCESS -> onReservationSuccess(it.data!!)
      }
    })
  }

  /** Load user's profile from a remote server (async)  */
  private fun getReservation() {
    if (edit_keyword_search.text.isNotEmpty()) key = edit_keyword_search.text.toString()

    var keySelectLocation =  ""
    var keySelectFacility =  ""

    if (Hawk.contains(Constant.KEY_SELECT_LOCATION)) {
      val selectLocation: SelectLocation = Hawk.get(Constant.KEY_SELECT_LOCATION)
      keySelectLocation = selectLocation.keyLocation.toString()
    }

    if (Hawk.contains(Constant.KEY_SELECT_RESERVATION_CLASSES)) {
      val selectFacility: SelectFacility = Hawk.get(Constant.KEY_SELECT_RESERVATION_CLASSES)
      keySelectFacility = selectFacility.keyFacility.toString()    }

    reservationsListViewModel.fetchFromRemote(keySelectLocation, keySelectFacility)
  }

  private fun onReservationLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        reservationsListViewModel.cancel()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onReservationSuccess(response: ReservationsResponse) {
    Common.dismissProgressDialog()
    if (response.reservationList != null) {
      val listItem : ArrayList<Reservation> = ArrayList()
      val reservation = Reservation()
      for (i in response.reservationList!!.indices) {
        reservation.id = i + 1
        reservation.comments = response.reservationList!![i].comments
        reservation.date = response.reservationList!![i].date
        reservation.time = response.reservationList!![i].time
        reservation.locationDetails = response.reservationList!![i].locationDetails
        reservation.facilityShortDescription = response.reservationList!![i].facilityShortDescription

        listItem.add(reservation)
      }
      Hawk.put((Constant.KEY_RESERVATION_LIST), response.reservationList)
      ReservationActivity.launchIntent(requireContext())
    }
  }

  private fun onReservationFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      Common.showMessageDialog(it, "Error", error.message)
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
                      resources.getString(R.string.date_filter_mm_dd_yyyy), mSelectedMonth, mSelectedDay,
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
                      resources.getString(R.string.date_filter_mm_dd_yyyy), mSelectedMonth, mSelectedDay,
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
    deleteHawksReservations()

    ed_start_date.setText("")
    ed_end_date.setText("")
    edit_keyword_search.setText("")

    recycler_select_facility_reservation.visibility = View.GONE
    txt_facility_reservation.visibility = View.VISIBLE

    recycler_select_location.visibility = View.GONE
    txt_select_location.visibility = View.VISIBLE

  }

  private fun initRecyclerFacility() {
    val adapterFacility = SelectFacilityAdapter()
    val listSelectFacility: ArrayList<SelectFacility> = Hawk.get(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)
    recycler_select_facility_reservation.visibility = View.GONE
    txt_facility_reservation.visibility = View.GONE

    val itemList: ArrayList<String> = ArrayList()
    for (i in listSelectFacility.indices) {
      itemList.add(listSelectFacility[i].valueFacility.toString())
    }
    tagFacility.setTags(itemList)

    recycler_select_facility_reservation.layoutManager = GridLayoutManager(requireContext(), 3)

    recycler_select_facility_reservation.isNestedScrollingEnabled = false
    mLayoutManager = recycler_select_facility_reservation.layoutManager


    adapterFacility.setDataList(listSelectFacility)
    recycler_select_facility_reservation.adapter = adapterFacility
    adapterFacility.notifyDataSetChanged()
  }

  private fun initRecyclerLocation() {
    val adapterLocation = SelectLocationAdapter()
    val listSelectLocation: ArrayList<SelectLocation> = Hawk.get(Constant.KEY_SELECT_LOCATION_LIST)

//    recycler_select_location.visibility = View.VISIBLE
    txt_select_location.visibility = View.GONE

    val itemList: ArrayList<String> = ArrayList()
    for (i in listSelectLocation.indices) {
      itemList.add(listSelectLocation[i].valueLocation.toString())
    }
    tagLocation.setTags(itemList)

//    recycler_select_location.layoutManager = GridLayoutManager(requireContext(), 3)

    recycler_select_location.layoutManager =
            LinearLayoutManager(
                    requireContext(), LinearLayoutManager.VERTICAL,
                    false
            )

    recycler_select_location.isNestedScrollingEnabled = false
    mLayoutManager = recycler_select_location.layoutManager


    adapterLocation.setDataList(listSelectLocation)
    recycler_select_location.adapter = adapterLocation
    adapterLocation.notifyDataSetChanged()
  }

  private fun deleteHawksReservations() {
    Hawk.delete(Constant.KEY_SELECT_RESERVATION_CLASSES)
    Hawk.delete(Constant.KEY_SELECT_LOCATION)
    Hawk.delete(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)
    Hawk.delete(Constant.KEY_SELECT_LOCATION_LIST)
  }
  override fun onClick(v: View?) {
    when (v) {
      btn_search -> {
        getReservation()
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
      btn_select_facility -> {
        SelectFacilityActivity.launchIntent(requireContext())
      }
      btn_select_location -> {
        SelectLocationActivity.launchIntent(requireContext())
      }

      recycler_select_facility_reservation -> {
        SelectFacilityActivity.launchIntent(requireContext())
      }

      recycler_select_location -> {
        SelectFacilityActivity.launchIntent(requireContext())
      }
    }
  }
}