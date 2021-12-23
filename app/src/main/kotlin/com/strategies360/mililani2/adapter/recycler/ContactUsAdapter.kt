package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.contact.AddressContact
import com.strategies360.mililani2.model.remote.newsletter.Newsletter
import kotlinx.android.synthetic.main.adapter_contact_us.view.*

class ContactUsAdapter : DataListRecyclerViewAdapter<AddressContact, ContactUsAdapter.ViewHolder>(), OnMapReadyCallback {

  var mLayoutManager: LayoutManager? = null
  private val mapView: MapView? = null
  private val gmap: GoogleMap? = null
  var onItemContactClick: onItemContactClick? = null
  var onItemMapsContactClick: onItemMapsContactClick? = null

  private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"


  override fun onCreateDataViewHolder(
          parent: ViewGroup,
          viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_contact_us))
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
      itemView.txtPhone.text = data.phone
      itemView.txtMapsTitle.text = data.mapTitle
      itemView.txtAddress.text = data.address

//      itemView.maps.getMapAsync(this);

      itemView.txtPhone.setOnClickListener {
        onItemContactClick?.invoke(adapterPosition, data)
      }

      itemView.btn_maps.setOnClickListener {
        onItemMapsContactClick?.invoke(adapterPosition, data)
      }

      val adapter = ContactUsScheduleAdapter()

      itemView.recycler_schedule.layoutManager =
        LinearLayoutManager(
                itemView.recycler_schedule.context, LinearLayoutManager.VERTICAL,
                false
        )

      itemView.recycler_schedule.isNestedScrollingEnabled = false
      mLayoutManager = itemView.recycler_schedule.layoutManager

      adapter.setDataList(data.recCenterHours)
      itemView.recycler_schedule.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  override fun onMapReady(p0: GoogleMap?) {

  }
}

typealias onItemContactClick = ((position: Int, data: AddressContact) -> Unit)
typealias onItemMapsContactClick = ((position: Int, data: AddressContact) -> Unit)
