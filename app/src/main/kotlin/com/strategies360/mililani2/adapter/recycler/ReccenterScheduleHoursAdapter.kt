package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.reservation.recCenters.DetailReccenterHour
import kotlinx.android.synthetic.main.adapter_rec_center_hour.view.*

class ReccenterScheduleHoursAdapter : DataListRecyclerViewAdapter<DetailReccenterHour, ReccenterScheduleHoursAdapter.ViewHolder>() {
  var mLayoutManager: LayoutManager? = null

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_rec_center_hour))
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

      if (data.hours != "") {
        itemView.txt_reccenter_schedule_day.text = data.day
        itemView.txt_reccenter_schedule_hour.text = "(" + data.hours + ")"
      } else {
        itemView.txt_reccenter_schedule_day.visibility = View.GONE
        itemView.txt_reccenter_schedule_hour.visibility = View.GONE
      }
    }
  }
}
