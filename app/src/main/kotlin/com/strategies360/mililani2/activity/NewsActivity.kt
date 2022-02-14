package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.adapter.recycler.NewsListAdapter
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import com.strategies360.mililani2.fragment.NewsFragment
import kotlinx.android.synthetic.main.activity_activities.btn_back
import kotlinx.android.synthetic.main.activity_activities.btn_scan_barcode
import javax.inject.Inject


class NewsActivity : CoreActivity() {

  @Inject
  override val viewRes: Int = R.layout.activity_news

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    btn_back.setOnClickListener {
      onBackPressed()
      finish()
    }

    btn_scan_barcode.setOnClickListener {
      openBottomCardList()
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager = supportFragmentManager
    MTACardBottomListFragment()
        .show(fragManager, "Dialog")
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */

    fun launchIntent(context: Context, position : Int? = 0) {
      val intent = Intent(context, NewsActivity::class.java)
      intent.putExtra(context.getString(R.string.prefs_index_news),
              position)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }
}