package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.id
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_bottom_nav_menu.nav_view

class BottomMenuNavigatonActivity : CoreActivity() {

  override val viewRes = R.layout.activity_bottom_nav_menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val navController = findNavController(id.nav_host_fragment)
    val appBarConfiguration = AppBarConfiguration(
        setOf(
            id.navigation_home, id.navigation_dashboard, id.navigation_notifications
        )
    )
    nav_view.itemIconTintList = null
    nav_view.setupWithNavController(navController)
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finishAffinity()
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, BottomMenuNavigatonActivity::class.java)
      context.startActivity(intent)
    }
  }
}