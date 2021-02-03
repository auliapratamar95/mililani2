package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.ActivitiesAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.mtaCard.Classes
import kotlinx.android.synthetic.main.adapter_activities.view.btn_layout_class
import kotlinx.android.synthetic.main.adapter_activities.view.img_calender
import kotlinx.android.synthetic.main.adapter_activities.view.layout_detail_class
import kotlinx.android.synthetic.main.adapter_activities.view.txt_date
import kotlinx.android.synthetic.main.adapter_activities.view.txt_day
import kotlinx.android.synthetic.main.adapter_activities.view.txt_description
import kotlinx.android.synthetic.main.adapter_activities.view.txt_status
import kotlinx.android.synthetic.main.adapter_activities.view.txt_title

class ActivitiesAdapter : DataListRecyclerViewAdapter<Classes, ViewHolder>() {
  private var tmpPosition = 0
//  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_activities))
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

      when {
        data.status.equals("Registered") -> {
          itemView.txt_status.text = data.status
          itemView.txt_status.background = App.context.getDrawable(R.drawable.bg_button_registered)
        }
        data.status.equals("Pending") -> {
          itemView.txt_status.text = data.status
          itemView.txt_status.background = App.context.getDrawable(R.drawable.bg_button_pending)
        }
        else -> {
          itemView.txt_status.text = data.status
          itemView.txt_status.background = App.context.getDrawable(R.drawable.bg_button_canceled)
        }
      }

      if (data.isCalender!!) {
        itemView.img_calender.setImageDrawable(App.context.getDrawable(R.drawable.ic_calender_active))
      } else {
        itemView.img_calender.setImageDrawable(App.context.getDrawable(R.drawable.ic_calender_nonactive))
      }

      itemView.txt_title.text = data.title
      itemView.txt_date.text = data.date
      itemView.txt_day.text = data.day
      itemView.txt_description.text = data.description
      itemView.btn_layout_class.setOnClickListener {
        if (itemView.layout_detail_class.visibility == View.GONE) {
          itemView.layout_detail_class.visibility = View.VISIBLE
        } else {
          itemView.layout_detail_class.visibility = View.GONE
        }
//        tmpPosition = adapterPosition
        notifyDataSetChanged()
//        isLayoutClickItem = true
      }

//      if (isLayoutClickItem) {
//        if (tmpPosition == adapterPosition) {
//            isLayoutClickItem = false
//            itemView.layout_detail_class.visibility = View.VISIBLE
//        } else {
//          itemView.layout_detail_class.visibility = View.GONE
//        }
//      } else {
//        itemView.layout_detail_class.visibility = View.GONE
//      }
    }
  }
}