package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.NewsListAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : CoreFragment(){

  var mLayoutManager: RecyclerView.LayoutManager? = null

  private var index: Int = -1

  override val viewRes: Int = R.layout.fragment_news

  override fun onViewCreated(
          view: View,

          savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    index = (requireActivity().intent.getIntExtra(getString(R.string.prefs_index_news), -1))

    if (Hawk.contains(Constant.KEY_LIST_NEWS)) {
      progress_setting.visibility = View.GONE
      val newsList: ArrayList<News> = Hawk.get(Constant.KEY_LIST_NEWS)
      val adapter = NewsListAdapter()

      if (index != -1) {
        for (i in newsList.indices) {
          if (index == i) {
            newsList[i].isExpandData = true
          }
        }
      }

      recycler_news.layoutManager =
              LinearLayoutManager(
                      recycler_news.context, LinearLayoutManager.VERTICAL,
                      false
              )

      recycler_news.isNestedScrollingEnabled = false
      mLayoutManager = recycler_news.layoutManager

      adapter.setDataList(newsList)
      recycler_news.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }
}