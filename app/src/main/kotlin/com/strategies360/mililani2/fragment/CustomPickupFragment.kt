package com.strategies360.mililani2.fragment

import android.R.color
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.style
import kotlinx.android.synthetic.main.layout_custom_pickup.btn_submit
import kotlinx.android.synthetic.main.layout_custom_pickup.select_time_pickup
import java.util.Calendar
import java.util.Objects

class CustomPickupFragment : BottomSheetDialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    setStyle(STYLE_NORMAL, style.BottomSheetDialogTheme)
    return inflater.inflate(layout.layout_custom_pickup, container, false)
  }

  override fun getTheme(): Int = style.BottomFilterSheetDialogTheme

  override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
    super.onCreate(savedInstanceState)
    val view = View.inflate(context, layout.layout_custom_pickup, null)
    val dialog = BottomSheetDialog(requireContext(), theme)
    dialog.setOnShowListener { dialogInterface ->
      val bottomSheetDialog = dialogInterface as BottomSheetDialog
      setupRatio(bottomSheetDialog)
    }
    dialog.setContentView(view)

    return dialog
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    select_time_pickup.setIs24HourView(false);
    btn_submit.setOnClickListener {
      var hour: Int
      val minute: Int
      val am_pm: String
      if (Build.VERSION.SDK_INT >= 23) {
        hour = select_time_pickup.hour
        minute = select_time_pickup.minute
      } else {
        hour = select_time_pickup.currentHour
        minute = select_time_pickup.currentMinute
      }
      if (hour > 12) {
        am_pm = "PM"
        hour = hour - 12
      } else {
        am_pm = "AM"
      }
      dismiss()
    }
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
    return getWindowHeight() * 45 / 100
  }

  private fun getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
  }

  private fun setPickupCheckout() {
    val calenderInstance: Calendar = Calendar.getInstance()
    val hr: Int = calenderInstance.get(Calendar.HOUR_OF_DAY)
    val min: Int = calenderInstance.get(Calendar.MINUTE)
    val onTimeListner = OnTimeSetListener { view, hourOfDay, minute ->
      if (view.isShown) {
        calenderInstance.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calenderInstance.set(Calendar.MINUTE, minute)
      }
    }
    val timePickerDialog = TimePickerDialog(
        requireContext(),
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        onTimeListner, hr, min, true
    )
    timePickerDialog.setTitle("Set Time")
    Objects.requireNonNull(timePickerDialog.window)?.setBackgroundDrawableResource(color.transparent)
    timePickerDialog.show()
  }
}