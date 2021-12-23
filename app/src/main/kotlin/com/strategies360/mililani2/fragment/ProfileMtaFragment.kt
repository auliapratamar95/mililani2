package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.BuildConfig
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.NotificationActivity
import com.strategies360.mililani2.activity.WebviewHistoryActivity
import com.strategies360.mililani2.activity.WebviewHouseholdActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.local.categoryquicklink.CategoryQuickLink
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.fragment_more_menu.*
import kotlinx.android.synthetic.main.fragment_profile_mta_card.*
import kotlinx.android.synthetic.main.fragment_profile_mta_card.btn_add_history

/**
 * A Profile activity.
 */
class ProfileMtaFragment : CoreFragment(), View.OnClickListener {
    private val ONESIGNAL_APP_ID = "96dfcb3d-199e-4dff-9f0f-65df16c0c2c4"
    var versionName: String = BuildConfig.VERSION_NAME

    override val viewRes = R.layout.fragment_profile_mta_card

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        switch1.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                OneSignal.setAppId(ONESIGNAL_APP_ID)
            } else {
                OneSignal.clearOneSignalNotifications()
                OneSignal.setAppId("0")
            }
        }

        txt_version.text = versionName

        btn_logout.setOnClickListener(this)
        btn_my_activities.setOnClickListener(this)
        btn_open_household.setOnClickListener(this)
//        btn_open_notification.setOnClickListener(this)
        btn_add_history.setOnClickListener(this)
        btn_add_household.setOnClickListener(this)
    }

    private fun openDialogSignout() {
        val fragManager: FragmentManager? = fragmentManager
        if (fragManager != null) {
            DialogConfirmationLogout()
                .show(fragManager, "Dialog")
        }
    }

    private fun openHistory() {
        WebviewHistoryActivity.launchIntent(requireContext())
    }

    private fun openHousehold() {
        WebviewHouseholdActivity.launchIntent(requireContext())
    }

    private fun openNotifications() {
        NotificationActivity.launchIntent(requireContext())
    }

    private fun saveCategoryQuickLink(id: Int, name: String) {
        var isCategoryAvailable = false
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val saveCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
            val getCategoryQuickLink: ArrayList<CategoryQuickLink> =
                Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)

            if (getCategoryQuickLink.size < 4) {
                for (i in getCategoryQuickLink.indices) {
                    if (id == getCategoryQuickLink[i].id) {
                        isCategoryAvailable = true
                        break
                    }
                }

                if (!isCategoryAvailable) {
                    val categoryQuickLink = CategoryQuickLink()
                    categoryQuickLink.id = id
                    categoryQuickLink.name = name
                    getCategoryQuickLink.add(categoryQuickLink)
                }
                saveCategoryQuickLink.addAll(getCategoryQuickLink)
                Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), saveCategoryQuickLink)
            }
        } else {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
            val categoryQuickLink = CategoryQuickLink()
            categoryQuickLink.id = id
            categoryQuickLink.name = name
            listCategoryQuickLink.add(categoryQuickLink)
            Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), listCategoryQuickLink)
        }
        showHideDialogQuickLink()
    }

    private fun showHideDialogQuickLink() {
        if (Hawk.contains(Constant.FLAG_ON_DIALOG)) {
            val isDialog: Boolean = Hawk.get(Constant.FLAG_ON_DIALOG)
            if (!isDialog) {
                openDialogQuickLink()
            }
        } else {
            openDialogQuickLink()
        }
//    getCategoryQuickLink()
    }

    private fun openDialogQuickLink() {
        val fragManager: FragmentManager? = fragmentManager
        if (fragManager != null) {
            DialogConfirmationQuickLink()
                .show(fragManager, "Dialog")
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            btn_logout -> {
                openDialogSignout()
            }
            btn_my_activities -> {
                openHistory()
            }
            btn_open_household -> {
                openHousehold()
//        requireActivity().finish()
            }
//            btn_open_notification -> {
//                openNotifications()
//            }
            btn_add_household -> {
                saveCategoryQuickLink(9, "household")
            }
            btn_history -> {
                saveCategoryQuickLink(10, "history")
            }
//      btn_caffe -> {
//        saveCategoryQuickLink(1, "Caffe")
//      }
        }
    }
}
