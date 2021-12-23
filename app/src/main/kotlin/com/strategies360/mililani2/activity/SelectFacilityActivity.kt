package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.model.remote.reservation.SelectFacility
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.activity_select_facility_classes.*

/**
 * A Submit Scan MTA Card activity.
 */
class SelectFacilityActivity : CoreActivity() {

  override val viewRes = R.layout.activity_select_facility_classes

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Hawk.delete(Constant.KEY_SELECT_RESERVATION_CLASSES)
    Hawk.delete(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)

    btn_submit.setOnClickListener {

      if (cb_courts.isChecked) getSelectFasilityClasses(cb_courts.text.toString(), getString(R.string.court))

      if (cb_hall_rentals.isChecked) getSelectFasilityClasses(cb_hall_rentals.text.toString(),getString(R.string.hall))

      if (cb_pool_lane.isChecked) getSelectFasilityClasses(cb_pool_lane.text.toString(),getString(R.string.lane))

      if (cb_water_aerobics.isChecked) getSelectFasilityClasses(cb_water_aerobics.text.toString(),getString(R.string.wa))

      onBackPressed()
    }

    btn_reset.setOnClickListener {
      cb_courts.isChecked = false
      cb_hall_rentals.isChecked = false
      cb_pool_lane.isChecked = false
      cb_water_aerobics.isChecked = false
    }
  }

  private fun getSelectFasilityClasses(title: String, choiceChecked: String) {
    val selectFacility = SelectFacility()
    var selectFacilityList = ArrayList<SelectFacility>()

    if (Hawk.contains(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)) {
      selectFacilityList = Hawk.get(Constant.KEY_SELECT_RESERVATION_CLASSES_LIST)
    }

    if (Hawk.contains(Constant.KEY_SELECT_RESERVATION_CLASSES)) {
      val constantFacilityClasses: SelectFacility = Hawk.get(Constant.KEY_SELECT_RESERVATION_CLASSES)
      val saveDataFacilityClasses = "${constantFacilityClasses.keyFacility},$choiceChecked"

      Hawk.delete((Constant.KEY_SELECT_LOCATION))
      selectFacility.keyFacility = saveDataFacilityClasses
      selectFacility.valueFacility = title
      Hawk.put((Constant.KEY_SELECT_RESERVATION_CLASSES), selectFacility)
    } else {
      selectFacility.keyFacility = choiceChecked
      selectFacility.valueFacility = title
      Hawk.put((Constant.KEY_SELECT_RESERVATION_CLASSES), selectFacility)
    }

    selectFacilityList.add(selectFacility)
    Hawk.put((Constant.KEY_SELECT_RESERVATION_CLASSES_LIST), selectFacilityList)

  }

  companion object {

    fun launchIntent(context: Context) {
      val intent = Intent(context, SelectFacilityActivity::class.java)
      context.startActivity(intent)
    }
  }
}
