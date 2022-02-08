package com.strategies360.mililani2.adapter.viewpager

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.NewsActivity
import com.strategies360.mililani2.model.remote.news.News
import kotlinx.android.synthetic.main.adapter_news.view.*

class ViewPagerNewsAdapter : PagerAdapter() {

  private var models: ArrayList<News> = ArrayList()
  private var context: Context? = null
  private lateinit var listener: onSelectedIndex

  fun viewPagerKotlinAdapter(
    models: ArrayList<News>,
    context: Context?,
    listener: onSelectedIndex
  ) {
    this.models = models
    this.context = context
    this.listener = listener
  }

  override fun getCount(): Int {
    return models.size
  }

  override fun isViewFromObject(
    view: View,
    `object`: Any
  ): Boolean {
    return view == `object`
  }

  override fun instantiateItem(
    container: ViewGroup,
    position: Int
  ): Any {
    val item =
      LayoutInflater.from(container.context).inflate(R.layout.adapter_news, container, false)

    item.title.text = models[position].postTitle
    item.desc.text = HtmlCompat.fromHtml(models[position].postContent.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

    item.btn_open_news.setOnClickListener {
//      NewsActivity.launchIntentWithIndex(App.context,position)
      models[position].isSelected = true
//      models.forEach { news ->
//        Log.i( "instantiateItem: ", news.isSelected.toString())
//      }
      listener.onClickIndex(position)
    }
    container.addView(item)
    return item
  }

  override fun destroyItem(
    container: ViewGroup,
    position: Int,
    `object`: Any
  ) {
    container.removeView(`object` as View)
  }

  interface onSelectedIndex {
    fun onClickIndex(position: Int)
  }
}