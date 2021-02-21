package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.ActivitiesActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_profile_mta_card.*

/**
 * A Profile activity.
 */
class ProfileMtaFragment : CoreFragment(), View.OnClickListener {

  override val viewRes = R.layout.fragment_profile_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    btn_logout.setOnClickListener(this)
    btn_my_activities.setOnClickListener(this)
  }

  private fun openDialogSignout() {
    val fragManager: FragmentManager? = fragmentManager
    if (fragManager != null) {
      DialogConfirmationLogout()
          .show(fragManager, "Dialog")
    }
  }

  private fun openActivitiesFragment() {
    ActivitiesActivity.launchIntent(requireContext())
  }

  override fun onClick(v: View?) {
    when (v) {
      btn_logout -> {
        openDialogSignout()
      }
      btn_my_activities -> {
        openActivitiesFragment()
        requireActivity().finish()
      }
    }
  }
}
