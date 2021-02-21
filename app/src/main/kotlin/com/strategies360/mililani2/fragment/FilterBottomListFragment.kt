package com.strategies360.mililani2.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.style
import kotlinx.android.synthetic.main.layout_dialog_filter.*
import java.util.*

class FilterBottomListFragment : BottomSheetDialogFragment(), View.OnClickListener {
  private var picker: DatePickerDialog? = null
  override fun getTheme(): Int = style.BottomSheetDialogTheme

  override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
    super.onCreate(savedInstanceState)
    val view = View.inflate(context, layout.layout_dialog_filter, null)
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
    return getWindowHeight() * 70 / 100
  }

  private fun getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
  }

  override fun onClick(view: View?) {
    if (view == btn_start_date) {
      val cldr: Calendar = Calendar.getInstance()
      val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
      val month: Int = cldr.get(Calendar.MONTH)
      val year: Int = cldr.get(Calendar.YEAR)
      // date picker dialog
      // date picker dialog
      picker = DatePickerDialog(
          requireContext(),
          { _, year, monthOfYear, dayOfMonth ->
            btn_start_date.setText(
                dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
            )
          }, year, month, day
      )
      picker!!.show()
    }
  }
}