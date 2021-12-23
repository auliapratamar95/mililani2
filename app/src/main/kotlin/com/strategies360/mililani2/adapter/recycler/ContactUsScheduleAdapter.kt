package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.contact.RecCenterHours
import kotlinx.android.synthetic.main.adapter_contact_us_schedule.view.*
import kotlinx.android.synthetic.main.adapter_detail_cart.view.*

class ContactUsScheduleAdapter : DataListRecyclerViewAdapter<RecCenterHours, ContactUsScheduleAdapter.ViewHolder>() {
  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_contact_us_schedule))
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
      itemView.txtDay.text = data.day
      itemView.txtHours.text = data.hours
    }
  }
}