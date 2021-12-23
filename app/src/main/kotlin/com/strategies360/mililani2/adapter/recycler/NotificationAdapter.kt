package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.notification.Notification
import kotlinx.android.synthetic.main.adapter_notification.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter : DataListRecyclerViewAdapter<Notification, NotificationAdapter.ViewHolder>() {

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_notification))
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
      val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
      val output: String = formatter.format(parser.parse(data.date))

      itemView.txt_date_notification.text = output
      itemView.txt_detail_notification.text = data.content
    }
  }
}