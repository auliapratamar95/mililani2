package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.activity.RecCenterActivity
import com.strategies360.mililani2.activity.RecCentersActivity
import com.strategies360.mililani2.activity.WebviewRegisterActivity
import com.strategies360.mililani2.adapter.recycler.ReservationAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.reservation.Reservation
import kotlinx.android.synthetic.main.adapter_all_activities.view.*
import kotlinx.android.synthetic.main.adapter_reservation.view.*
import kotlinx.android.synthetic.main.adapter_reservation.view.txt_comment
import kotlinx.android.synthetic.main.adapter_reservation.view.txt_date
import kotlinx.android.synthetic.main.adapter_reservation.view.txt_day
import kotlinx.android.synthetic.main.adapter_reservation.view.txt_location
import kotlinx.android.synthetic.main.adapter_reservation.view.txt_title

class ReservationAdapter : DataListRecyclerViewAdapter<Reservation, ViewHolder>() {

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_reservation))
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
      val comment = HtmlCompat.fromHtml(data.comments.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

      itemView.txt_title.text = data.facilityShortDescription
      itemView.txt_location.text = HtmlCompat.fromHtml(data.locationDetails.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

      if (data.date != "") {
        itemView.txt_date.text = data.date
      } else {
        itemView.txt_date.text = "5:30 pm"
      }

      if (data.time != "") {
        itemView.txt_day.text = data.time
      } else {
        itemView.txt_day.text = "March 21, 2020"
      }

      if (comment != "") {
        itemView.txt_comment.text = HtmlCompat.fromHtml(data.comments.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
      } else {
        itemView.txt_comment.text = "Event: Movie by the Pool - SPIDER-MAN: INTO THE SPIDER-VERSE\n" +
                "Event Date: March 21, 2020\n" +
                "Event Time: Doors open at 5:30 pm\n" +
                "Event Location: Rec 1 | 95-400 Ikaloa Street • Mililani, HI 96789\n" +
                "\n" +
                "Event Ticket Release Date: February 29, 2020\n" +
                "Member Ticket Price: \$2 per MTA Member\n" +
                "Number of Tickets: 450\n" +
                "Rules (Optional):\n" +
                "\n" +
                "Guest Ticket Release Date: March 7, 2020\n" +
                "Guest Ticket Price: \$4 per person\n" +
                "\n" +
                "Event Blurb (Sponsors, features, etc.): Dinner by Pololi’Oe, Popcorn, Uncle Lani’s Poi Mochi, Hot Cocoa\n" +
                "\n" +
                "Other: No entry if event is SOLD OUT and at capacity.\n"
      }

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

      itemView.btn_view_map.setOnClickListener{
        RecCenterActivity.launchIntent(App.context)
      }
    }
  }
}