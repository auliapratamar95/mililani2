package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.activity.WebviewRegisterActivity
import com.strategies360.mililani2.adapter.recycler.AllActivitiesAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.mtaCard.Classes
import kotlinx.android.synthetic.main.adapter_all_activities.view.btn_layout_class
import kotlinx.android.synthetic.main.adapter_all_activities.view.btn_register
import kotlinx.android.synthetic.main.adapter_all_activities.view.layout_detail_activities
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_comment
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_date
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_day
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_elgibility
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_enrollment_max_count
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_enrollment_total_count
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_fee
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_location
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_meeting_details
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_status
import kotlinx.android.synthetic.main.adapter_all_activities.view.txt_title

class AllActivitiesAdapter : DataListRecyclerViewAdapter<Classes, ViewHolder>() {
  private var tmpPosition = 0
  private var isLayoutClickItem = false

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_all_activities))
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

      itemView.txt_elgibility.text = data.elgibility
      itemView.txt_meeting_details.text = HtmlCompat.fromHtml(data.meetingDetails.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
      itemView.txt_location.text = data.location
      itemView.txt_fee.text = "$" + data.fee
      itemView.txt_comment.text = data.comment
      itemView.txt_enrollment_total_count.text = "Overall Total Enrolled: " + data.totalCount
      itemView.txt_enrollment_max_count.text = "Overall Max Count: " + data.maxCount

      itemView.txt_title.text = data.description
      itemView.txt_date.text = data.date
      itemView.txt_day.text = data.day

      itemView.btn_layout_class.setOnClickListener {
        if (itemView.layout_detail_activities.visibility == View.GONE) {
          itemView.layout_detail_activities.visibility = View.VISIBLE
        } else {
          itemView.layout_detail_activities.visibility = View.GONE
        }
        notifyDataSetChanged()
      }

      itemView.btn_register.setOnClickListener{
        WebviewRegisterActivity.launchIntent(App.context)
      }
    }
  }
}