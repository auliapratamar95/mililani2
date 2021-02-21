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

  override val viewRes = R.layout.activity_bottom_nav_menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val navController = findNavController(id.nav_host_fragment)
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

    fun launchIntent(context: Context, isBottomCardList: Boolean) {
      val intent = Intent(context, BottomMenuNavigationActivity::class.java)
      intent.putExtra(context.getString(string.prefs_is_bottom_card_list),
          isBottomCardList)
      context.startActivity(intent)
    }
  }
}