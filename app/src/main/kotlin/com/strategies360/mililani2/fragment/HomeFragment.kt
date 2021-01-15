package com.strategies360.mililani2.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.LoginPhoneNumberActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.fragment_home.btn_logout
import kotlinx.android.synthetic.main.fragment_home.btn_scan_barcode

class HomeFragment : CoreFragment(), View.OnClickListener {

  private var isBottomCardList = false
  override val viewRes: Int? = layout.fragment_home

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    Hawk.put((Constant.FLAG_ON_BACK_MENU), true)
    isBottomCardList = (requireActivity().intent.getBooleanExtra(getString(string.prefs_is_bottom_card_list), false))

    if (isBottomCardList) openBottomCardList()

    btn_logout.setOnClickListener(this)
    btn_scan_barcode.setOnClickListener(this)
  }

  private fun showMessageDialog(
    dismissListener: DialogInterface.OnDismissListener? = null
  ) {
    val builder = AlertDialog.Builder(requireContext(), R.style.AppTheme_Dialog_Alert)
    builder.setMessage(getString(string.close_apps))
    builder.setPositiveButton(android.R.string.yes)
    { dialog, _ ->
      dialog.dismiss()
      LoginPhoneNumberActivity.launchIntent(requireContext())
    }
    builder.setNegativeButton(
        android.R.string.no
    ) { dialog, _ -> dialog.dismiss() }

    val dialog = builder.create()
    if (dismissListener != null) {
      dialog.setOnDismissListener(dismissListener)
      dialog.setCancelable(false)
      dialog.setCanceledOnTouchOutside(false)
    }
    dialog.show()
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      MTACardBottomListFragment()
          .show(fragManager, "Dialog")
    }
  }

  private fun openDialogSignout() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      DialogConfirmationLogout()
          .show(fragManager, "Dialog")
    }
  }

  override fun onClick(view: View?) {
    when (view) {
      btn_logout -> {
        openDialogSignout()
      }
      btn_scan_barcode -> {
        openBottomCardList()
      }
    }
  }
}