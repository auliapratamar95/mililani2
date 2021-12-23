package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.id
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.activity_bottom_nav_menu.*


class BottomMenuNavigationActivity : CoreActivity() {

  private var isSpecificMenu: String? = null
  override val viewRes = R.layout.activity_bottom_nav_menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    isSpecificMenu = (this.intent.getStringExtra(getString(string.prefs_is_specific_menu)))

    val navController = findNavController(id.nav_host_fragment)
    when (isSpecificMenu) {
        "classes" -> {
          navController.navigate(id.navigation_classes)
        }
        "events" -> {
          navController.navigate(id.navigation_events)
        }
        "setting" -> {
          navController.navigate(id.navigation_setting)
        }
        "reservation" -> {
          navController.navigate(id.navigation_schedule)
        }
        else -> {
          navController.navigate(id.navigation_home)
        }
    }

    nav_view.itemIconTintList = null
    nav_view.setupWithNavController(navController)

  }

  override fun onBackPressed() {
    if (Hawk.contains(Constant.FLAG_ON_BACK_MENU)) {
      if (Hawk.get(Constant.FLAG_ON_BACK_MENU)) {
        finishAffinity()
      } else {
        launchIntent(this)
      }
    } else {
      finishAffinity()
    }
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, BottomMenuNavigationActivity::class.java)
      context.startActivity(intent)
    }

    fun launchIntent(context: Context, isSpecificMenu: String) {
      Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
      val intent = Intent(context, BottomMenuNavigationActivity::class.java)
      intent.putExtra(context.getString(string.prefs_is_specific_menu),
              isSpecificMenu)
      context.startActivity(intent)
    }

    fun launchIntent(context: Context, isBottomCardList: Boolean) {
      val intent = Intent(context, BottomMenuNavigationActivity::class.java)
      intent.putExtra(context.getString(string.prefs_is_bottom_card_list),
              isBottomCardList)
      context.startActivity(intent)
    }
  }
}