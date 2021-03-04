package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import kotlinx.android.synthetic.main.fragment_sample_detail_product.appbar
import kotlinx.android.synthetic.main.fragment_sample_detail_product.btn_back

class SampleCategory: CoreActivity() {
  override val viewRes: Int
    get() = R.layout.fragment_sample_detail_product

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
      //measuring for alpha
      btn_back.visibility = View.VISIBLE
    })


    val collapsingToolbarLayout =
      findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
    collapsingToolbarLayout.title = "Dicoding"



    collapsingToolbarLayout.setCollapsedTitleTextColor(
        ContextCompat.getColor(this, R.color.white)
    )
    collapsingToolbarLayout.setExpandedTitleColor(
        ContextCompat.getColor(this, R.color.transparent)
    )
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, SampleCategory::class.java)
      context.startActivity(intent)
    }
  }

}