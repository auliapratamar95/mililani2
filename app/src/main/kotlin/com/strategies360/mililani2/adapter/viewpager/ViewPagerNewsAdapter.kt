package com.strategies360.mililani2.adapter.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import com.strategies360.mililani2.R
import com.strategies360.mililani2.model.remote.news.News
import kotlinx.android.synthetic.main.adapter_news.view.desc
import kotlinx.android.synthetic.main.adapter_news.view.title

class ViewPagerNewsAdapter: PagerAdapter() {

  private var models: ArrayList<News> = ArrayList()
  private var context: Context? = null

  fun viewPagerKotlinAdapter(
    models: ArrayList<News>,
    context: Context?
  ) {
    this.models = models
    this.context = context
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
    val item = LayoutInflater.from(container.context).inflate(R.layout.adapter_news, container, false)

    item.title.text = models[position].postTitle
    item.desc.text = HtmlCompat.fromHtml(models[position].postContent.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

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
}