package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.model.remote.reservation.SelectFacility
import com.strategies360.mililani2.model.remote.reservation.SelectLocation
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.activity_select_location_reservation.*

/**
 * A Submit Scan MTA Card activity.
 */
class SelectLocationActivity : CoreActivity() {
    override val viewRes = R.layout.activity_select_location_reservation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Hawk.delete(Constant.KEY_SELECT_LOCATION)
        Hawk.delete(Constant.KEY_SELECT_LOCATION_LIST)

        btn_submit.setOnClickListener {

            if (cb_mta_rec_1.isChecked) getSelectLocation(cb_mta_rec_1.text.toString(), getString(R.string.REC1))

            if (cb_mta_rec_2.isChecked) getSelectLocation(cb_mta_rec_2.text.toString(), getString(R.string.REC2))

            if (cb_mta_rec_3.isChecked) getSelectLocation(cb_mta_rec_3.text.toString(), getString(R.string.REC3))

            if (cb_mta_rec_4.isChecked) getSelectLocation(cb_mta_rec_4.text.toString(), getString(R.string.REC4))

            if (cb_mta_rec_5.isChecked) getSelectLocation(cb_mta_rec_5.text.toString(), getString(R.string.REC5))

            if (cb_mta_rec_6.isChecked) getSelectLocation(cb_mta_rec_6.text.toString(), getString(R.string.REC6))

            onBackPressed()
        }

        btn_reset.setOnClickListener {
            cb_mta_rec_1.isChecked = false
            cb_mta_rec_2.isChecked = false
            cb_mta_rec_3.isChecked = false
            cb_mta_rec_4.isChecked = false
            cb_mta_rec_5.isChecked = false
            cb_mta_rec_6.isChecked = false
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getSelectLocation(title: String, choiceChecked: String) {
        var selectLocation = SelectLocation()
        var selectLocationList = ArrayList<SelectLocation>()

        if (Hawk.contains(Constant.KEY_SELECT_LOCATION_LIST)) {
            selectLocationList = Hawk.get(Constant.KEY_SELECT_LOCATION_LIST)
        }

        if (Hawk.contains(Constant.KEY_SELECT_LOCATION)) {
            selectLocation = Hawk.get(Constant.KEY_SELECT_LOCATION)
            val saveDataLocation = "${selectLocation.keyLocation},$choiceChecked"

            Hawk.delete((Constant.KEY_SELECT_LOCATION))
            selectLocation.keyLocation = saveDataLocation
            selectLocation.valueLocation = title
            Hawk.put((Constant.KEY_SELECT_LOCATION), selectLocation)
        } else {
            selectLocation.keyLocation = choiceChecked
            selectLocation.valueLocation = title
            Hawk.put((Constant.KEY_SELECT_LOCATION), selectLocation)
        }
        selectLocationList.add(selectLocation)
        Hawk.put((Constant.KEY_SELECT_LOCATION_LIST), selectLocationList)
    }


    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, SelectLocationActivity::class.java)
            context.startActivity(intent)
        }
    }
}
