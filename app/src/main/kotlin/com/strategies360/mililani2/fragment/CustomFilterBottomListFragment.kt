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
import com.strategies360.mililani2.activity.ActivitiesActivity
import com.strategies360.mililani2.eventbus.EventFilterResult
import com.strategies360.mililani2.widget.DatePickerDialog
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.layout_custom_dialog_filter.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CustomFilterBottomListFragment : BottomSheetDialogFragment(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

  private var mSelectedDay = -1
  private var mSelectedMonth = -1
  private var mSelectedYear = -1

  private var startDate: String? = ""
  private var endDate: String? = ""
  private var isDateFilter = false

  private var edStartDate: String? = ""
  private var edEndDate: String? = ""

  private var isCheckedAquatics = false

  private var listActivityType: ArrayList<String> = ArrayList()
  private var listSubTypeAquatics: ArrayList<String> = ArrayList()
  private var listSubTypeSeniors: ArrayList<String> = ArrayList()
  private var listSubTypeTinyTots: ArrayList<String> = ArrayList()
  private var listSubTypeWomen: ArrayList<String> = ArrayList()

  var listHash: HashMap<String, String> = HashMap()

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
    btn_show.setOnClickListener(this)

    ed_start_date.keyListener = null
    ed_end_date.keyListener = null

    checkedAquatics()
    checkedSeniors()
    checkedTinyTots()
    checkedWomenExercise()

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
      btn_show -> {
        if (tag.equals("activities")) {
          ActivitiesActivity.launchIntent(requireContext())
          requireActivity().finish()
        } else {
          submitDataFilter()
        }
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

      edStartDate = startDate
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
      edEndDate = endDate
    }
  }

  override fun onCancelled(dialog: DatePickerDialog?) {}

  private fun checkedAquatics() {
    cb_aquatics_adult.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics_master.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics_mlearn.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics_one.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics_parent.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics_specials.setOnCheckedChangeListener { compoundButton, b ->
      cb_aquatics.isChecked = true
      isCheckedAquatics = true
    }

    cb_aquatics.setOnCheckedChangeListener { compoundButton, b ->
      if(!cb_aquatics.isChecked) {
        cb_aquatics_adult.isChecked = false
        cb_aquatics_specials.isChecked = false
        cb_aquatics_parent.isChecked = false
        cb_aquatics_one.isChecked = false
        cb_aquatics_master.isChecked = false
        cb_aquatics_mlearn.isChecked = false
        cb_aquatics.isChecked = false
      }
    }
  }

  private fun checkedSeniors() {
    cb_seniors_detail.setOnCheckedChangeListener { compoundButton, b ->
      cb_seniors.isChecked = true
    }

    cb_seniors.setOnCheckedChangeListener { compoundButton, b ->
      if(!cb_seniors.isChecked) {
        cb_seniors_detail.isChecked = false
        cb_seniors.isChecked = false
      }
    }
  }

  private fun checkedTinyTots() {
    cb_tiny_tots_detail.setOnCheckedChangeListener { compoundButton, b ->
      cb_tiny_tots.isChecked = true
    }

    cb_tiny_tots.setOnCheckedChangeListener { compoundButton, b ->
      if(!cb_tiny_tots.isChecked) {
        cb_tiny_tots_detail.isChecked = false
        cb_tiny_tots.isChecked = false
      }
    }
  }

  private fun checkedWomenExercise() {
    cb_womens_detail.setOnCheckedChangeListener { compoundButton, b ->
      cb_women.isChecked = true
    }

    cb_women.setOnCheckedChangeListener { compoundButton, b ->
      if (!cb_women.isChecked) {
        cb_womens_detail.isChecked = false
        cb_women.isChecked = false
      }
    }
  }

  private fun submitDataFilter() {
    var detailAquatics = ""
    if (cb_aquatics.isChecked) {
      var isCheckedDetailAquatics = false
      if (cb_aquatics_adult.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "Adult LTS,$detailAquatics"
        } else {
          "Adult LTS"
        }
        listSubTypeAquatics.add("Adult LTS")
      }

      if (cb_aquatics_master.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "JR Masters,$detailAquatics"
        } else {
          "JR Masters"
        }
        listSubTypeAquatics.add("JR Masters")
      }

      if (cb_aquatics_mlearn.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "Learn To Swim,$detailAquatics"
        } else {
          "Learn To Swim"
        }
        listSubTypeAquatics.add("Learn To Swim")
      }

      if (cb_aquatics_one.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "One On One LTS,$detailAquatics"
        } else {
          "One On One LTS"
        }
        listSubTypeAquatics.add("One On One LTS")
      }

      if (cb_aquatics_parent.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "Parent Aid,$detailAquatics"
        } else {
          "Parent Aid"
        }
        listSubTypeAquatics.add("Parent Aid")
      }

      if (cb_aquatics_parent.isChecked) {
        isCheckedDetailAquatics = true
        detailAquatics = if (detailAquatics != "") {
          "Specials Needs LTS,$detailAquatics"
        } else {
          "Specials Needs LTS"
        }
        listSubTypeAquatics.add("Specials Needs LTS")
      }

      if (isCheckedDetailAquatics) listActivityType.add("Aquatics")
    }
    if (cb_seniors.isChecked) {
      var isCheckedDetailSeniors = false
      if (cb_seniors_detail.isChecked) {
        isCheckedDetailSeniors = true
        listSubTypeSeniors.add("Seniors")
      }
      if (isCheckedDetailSeniors) listActivityType.add("Seniors")
    }
    if (cb_tiny_tots.isChecked) {
      var isCheckedDetailTiny = false
      if (cb_tiny_tots_detail.isChecked) {
        isCheckedDetailTiny = true
        listSubTypeTinyTots.add("Tiny Tots")
      }
      if (isCheckedDetailTiny) listActivityType.add("Tiny Tots")
    }
    if (cb_women.isChecked) {
      var isCheckedDetailWomen = false
      if (cb_womens_detail.isChecked) {
        isCheckedDetailWomen = true
        listSubTypeWomen.add("Women Exercise")
      }
      if (isCheckedDetailWomen) listActivityType.add("Women Exercise")
    }

    if (listActivityType.size != 0) {
      for (i in listActivityType.indices) {
        listHash["activity_type[$i]"] = listActivityType[i]
        when {
            listActivityType[i] == "Aquatics" -> {
              if (detailAquatics != "") {
               listHash["sub_type[0]"] = detailAquatics
              }
            }
            listActivityType[i] == "Seniors" -> {
              for (i in listSubTypeSeniors.indices) {
                if (listSubTypeSeniors.size != 0) listHash["sub_type[1]"] = listSubTypeSeniors[i]
              }
            }
            listActivityType[i] == "Seniors" -> {
              for (i in listSubTypeTinyTots.indices) {
                if (listSubTypeTinyTots.size != 0) listHash["sub_type[2]"] = listSubTypeTinyTots[i]
              }
            }
            else -> {
              for (i in listSubTypeTinyTots.indices) {
                if (listSubTypeWomen.size != 0) listHash["sub_type[3]"] = listSubTypeWomen[i]
              }
            }
        }
      }
      EventBus.getDefault().postSticky(
              EventFilterResult(listHash)
      )
      dismiss()
    }
  }
}