package com.strategies360.mililani2.view.spinner

import android.content.Context
import android.util.AttributeSet
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.view.spinner.core.CoreTextInputSpinner
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class SampleProductTextInputSpinner : CoreTextInputSpinner<String> {

  constructor(context: Context) : super(context)

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs)

  override fun onPrepare() {
    super.onPrepare()
    initSpinner()
  }

  private fun initSpinner() {
    setTextInputHint("Select Time")
    if (Hawk.contains(Constant.KEY_PICKUP_TIME)) {
      val simpleData: ArrayList<String> = Hawk.get(Constant.KEY_PICKUP_TIME)
      val listPickupTime: ArrayList<String> = ArrayList()
      for (i in simpleData.indices) {
        val sdf = SimpleDateFormat("hh:mm")
        val sdfs = SimpleDateFormat("hh:mm aa")
        val dt: Date = sdf.parse(simpleData[i]);
        val time1: String = sdfs.format(dt)
        listPickupTime.add(time1)
      }
      data = listPickupTime
      showPlaceholder()
    }
  }
}
