package com.strategies360.mililani2.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.activity.WebviewFacilityScheduleActivity
import com.strategies360.mililani2.adapter.recycler.ContactUsScheduleAdapter
import com.strategies360.mililani2.adapter.recycler.ReccenterScheduleHoursAdapter
import com.strategies360.mililani2.adapter.recycler.RecretionCenterAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.remote.reservation.recCenters.DetailReccenterHour
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCenter
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_contact_us.*
import kotlinx.android.synthetic.main.adapter_contact_us.view.*
import kotlinx.android.synthetic.main.fragment_custom_rec_centers.*
import kotlinx.android.synthetic.main.fragment_custom_rec_centers.txtPhone


class RecCentersFragment : CoreFragment(), View.OnClickListener {

  var mLayoutManager: RecyclerView.LayoutManager? = null

  override val viewRes: Int
    get() = R.layout.fragment_custom_rec_centers

  override fun onViewCreated(
          view: View,
          savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initView()
    val recCenter: RecCenter = Hawk.get(Constant.KEY_REC_CENTER_DETAIL)
    btn_classes.setOnClickListener(this)
    btn_events.setOnClickListener(this)
    btn_reservations.setOnClickListener(this)
    btn_rec_Center_rules.setOnClickListener(this)
    btn_facility_rental_rules.setOnClickListener(this)
    btn_facility_schedule.setOnClickListener(this)
//    txtPhone.setOnClickListener {
//      val intent = Intent(Intent.ACTION_CALL)
//      intent.data = Uri.parse("tel:${recCenter.phone}")
//      startActivity(intent)
//    }
    txtPhone.setOnClickListener(this)
  }

  private fun initView() {
    if (Hawk.contains(Constant.KEY_REC_CENTER_DETAIL)) {
      val recCenter: RecCenter = Hawk.get(Constant.KEY_REC_CENTER_DETAIL)
      img_rec_Center.load(recCenter.image)
      txt_title.text = recCenter.postTitle
      txtPhone.text = recCenter.phone
      txt_date.text = recCenter.acf?.address?.address
      txt_description.text = HtmlCompat.fromHtml(
              recCenter.acf?.description.toString(),
              HtmlCompat.FROM_HTML_MODE_LEGACY)

      val adapter = ReccenterScheduleHoursAdapter()

      rec_center_schedule_ist.layoutManager =
        LinearLayoutManager(
          rec_center_schedule_ist.context, LinearLayoutManager.VERTICAL,
          false
        )

      rec_center_schedule_ist.isNestedScrollingEnabled = false
      mLayoutManager = rec_center_schedule_ist.layoutManager

      adapter.setDataList(recCenter.acf?.recCenterSchedule)
      rec_center_schedule_ist.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  override fun onClick(view: View?) {
    val recCenter: RecCenter = Hawk.get(Constant.KEY_REC_CENTER_DETAIL)
    if (view == btn_classes) {
      BottomMenuNavigationActivity.launchIntent(requireContext(), "classes")
    } else if (view == btn_events) {
      BottomMenuNavigationActivity.launchIntent(requireContext(), "events")
    } else if (view == btn_rec_Center_rules) {
      WebviewFacilityScheduleActivity.launchIntent(requireContext())
    } else if (view == btn_facility_rental_rules) {
      WebviewFacilityScheduleActivity.launchIntent(requireContext())
    } else if (view == btn_facility_schedule) {
      if (Hawk.contains(Constant.KEY_POOL_SCHEDULE)) {
        val url: String = Hawk.get(Constant.KEY_POOL_SCHEDULE)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
      }
    } else if (view == btn_reservations) {
      BottomMenuNavigationActivity.launchIntent(requireContext(), "reservation")
    } else if (view == txtPhone){
      val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${recCenter.phone}"))
      startActivity(intent)
    }
  }
}