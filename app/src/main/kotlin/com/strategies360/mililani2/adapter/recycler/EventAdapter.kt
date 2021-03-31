package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.activity.WebviewRegisterActivity
import com.strategies360.mililani2.adapter.recycler.EventAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.tickets.Events
import kotlinx.android.synthetic.main.adapter_tickets.view.btn_book_now
import kotlinx.android.synthetic.main.adapter_tickets.view.btn_layout_tickets
import kotlinx.android.synthetic.main.adapter_tickets.view.layout_detail_tickets
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_comment
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_date
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_day
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_elgibility
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_retrication
import kotlinx.android.synthetic.main.adapter_tickets.view.txt_title

class EventAdapter : DataListRecyclerViewAdapter<Events, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_tickets))
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

      itemView.txt_title.text = data.shortDescription
      itemView.txt_day.text = data.beginEventDate
      itemView.txt_elgibility.text = "-"
      itemView.txt_comment.text = HtmlCompat.fromHtml(data.brochureText.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
      itemView.txt_retrication.text = "-"

      itemView.txt_date.text = data.eventTime

      itemView.btn_layout_tickets.setOnClickListener {
        if (itemView.layout_detail_tickets.visibility == View.GONE) {
          itemView.layout_detail_tickets.visibility = View.VISIBLE
        } else {
          itemView.layout_detail_tickets.visibility = View.GONE
        }
        notifyDataSetChanged()
      }

      itemView.btn_book_now.setOnClickListener{
        WebviewRegisterActivity.launchIntent(App.context)
      }
    }
  }
}