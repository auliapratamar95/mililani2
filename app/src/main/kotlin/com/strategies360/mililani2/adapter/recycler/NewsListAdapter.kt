package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.news.News
import kotlinx.android.synthetic.main.adapter_list_news.view.*

class NewsListAdapter() : DataListRecyclerViewAdapter<News, NewsListAdapter.ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false
  var mLayoutManager: LayoutManager? = null
  var newsList = getDataList()

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_list_news))
  }

  override fun onBindDataViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bindView()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bindView() {
      val data = getDataList()[adapterPosition]
      itemView.txt_title.text = data.postTitle
      itemView.txt_content_news.text = HtmlCompat.fromHtml(
              data.postContent.toString(),
              HtmlCompat.FROM_HTML_MODE_LEGACY)

      if (data.isExpandData == false) {
        itemView.txt_content_news.visibility = View.GONE
        itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_plus))
      } else {
        itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_min))
        itemView.txt_content_news.visibility = View.VISIBLE
      }

      itemView.btn_category_detail.setOnClickListener {
        if (itemView.txt_content_news.visibility == View.GONE) {
          itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_min))
          itemView.txt_content_news.visibility = View.VISIBLE
        } else {
          itemView.ic_plus_min_date.setImageDrawable(App.context.getDrawable(R.drawable.ic_plus))
          itemView.txt_content_news.visibility = View.GONE
        }
        tmpPosition = adapterPosition
        isLayoutClickItem = true
      }
    }
  }

}