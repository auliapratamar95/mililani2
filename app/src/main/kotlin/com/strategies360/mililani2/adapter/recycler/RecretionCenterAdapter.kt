package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.activity.RecCentersActivity
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.contact.AddressContact
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCenter
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.adapter_rec_center.view.*

class RecretionCenterAdapter : DataListRecyclerViewAdapter<RecCenter, RecretionCenterAdapter.ViewHolder>() {
    var onItemPhoneClick: onItemPhoneClick? = null

    override fun onCreateDataViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(layout.adapter_rec_center))
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
            itemView.imgRecCenter.load(data.image)
            itemView.txtDescription.text = data.acf?.address?.address
            itemView.txtPhone.text = data.phone

            itemView.txtPhone.setOnClickListener {
                onItemPhoneClick?.invoke(adapterPosition, data)
            }

            itemView.btn_layout_rec_center.setOnClickListener {
                Hawk.put((Constant.KEY_REC_CENTER_DETAIL), data)
                RecCentersActivity.launchIntent(App.context)
            }
        }
    }
}

typealias onItemPhoneClick = ((position: Int, data: RecCenter) -> Unit)
