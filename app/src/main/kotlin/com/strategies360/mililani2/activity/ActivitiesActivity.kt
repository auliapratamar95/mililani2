package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity

class ActivitiesActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_activities

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, ActivitiesActivity::class.java)
      context.startActivity(intent)
    }
  }
}