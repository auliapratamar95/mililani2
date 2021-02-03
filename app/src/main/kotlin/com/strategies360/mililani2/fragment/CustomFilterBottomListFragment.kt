package com.strategies360.mililani2.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.style
import com.strategies360.mililani2.widget.DatePickerDialog
import kotlinx.android.synthetic.clearFindViewByIdCache
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.btn_category
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.btn_date_range
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.btn_location
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.btn_reset
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.ed_end_date
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.ed_start_date
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.ic_plus_min_category
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.ic_plus_min_date
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.ic_plus_min_location
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.layout_category
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.layout_date_range
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.layout_location
import java.util.Calendar

class CustomFilterBottomListFragment : BottomSheetDialogFragment(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

  private var mSelectedDay = -1
  private var mSelectedMonth = -1
  private var mSelectedYear = -1

  private var startDate: String? = ""
  private var endDate: String? = ""
  private var isDateFilter = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    setStyle(STYLE_NORMAL, style.BottomSheetDialogTheme)
    return inflater.inflate(layout.layout_custom_dialog_filter, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    ed_start_date.setOnClickListener(this)
    ed_end_date.setOnClickListener(this)
    btn_location.setOnClickListener(this)
    btn_date_range.setOnClickListener(this)
    btn_category.setOnClickListener(this)
    btn_reset.setOnClickListener(this)

    ed_start_date.keyListener = null
    ed_end_date.keyListener = null

    ed_start_date.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        showDatePicker(true)
      }
    }

    ed_end_date.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        showDatePicker(false)
      }
    }
  }

  override fun getTheme(): Int = style.BottomFilterSheetDialogTheme

  override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
    super.onCreate(savedInstanceState)
    val view = View.inflate(context, layout.layout_custom_dialog_filter, null)
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
    return getWindowHeight() * 80 / 100
  }

  private fun getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
  }

  @SuppressLint("SetTextI18n")
  override fun onClick(view: View?) {
    when (view) {
      ed_start_date -> {
        showDatePicker(true)
      }
      ed_end_date -> {
        showDatePicker(false)
      }
      btn_date_range -> {
        if (layout_date_range.visibility == View.GONE) {
          ic_plus_min_date.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
          slideDown(layout_date_range)
        } else {
          ic_plus_min_date.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
          slideUp(layout_date_range)
        }
      }
      btn_location -> {
        if (layout_location.visibility == View.GONE) {
          ic_plus_min_location.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
          slideDown(layout_location)
        } else {
          ic_plus_min_location.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
          slideUp(layout_location)
        }
      }
      btn_category -> {
        if (layout_category.visibility == View.GONE) {
          ic_plus_min_category.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
          slideDown(layout_category)
        } else {
          ic_plus_min_category.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
          slideUp(layout_category)
        }
      }
      btn_reset -> {
        clearDataValue()
      }
    }
  }

  private fun clearDataValue() {
    ed_start_date.setText("")
    ed_end_date.setText("")
  }

  // slide the view from below itself to the current position
  private fun slideUp(view: View) {
    view.animate()
        .translationY(5f)
        .setDuration(500)
        .setListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            view.visibility = View.GONE
          }
        })
    view.clearFindViewByIdCache()
  }

  // slide the view from its current position to below itself
  private fun slideDown(view: View) {
    view.visibility = View.VISIBLE
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
    mSelectedDay = dayOfMonth
    mSelectedMonth = monthOfYear + 1
    mSelectedYear = year

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
    }
  }

  override fun onCancelled(dialog: DatePickerDialog?) {}
}