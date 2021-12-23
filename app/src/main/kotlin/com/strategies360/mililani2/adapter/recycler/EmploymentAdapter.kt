package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.employment.contentemployment.InnerContenAccordionEmployment
import kotlinx.android.synthetic.main.adapter_employment.view.*

class EmploymentAdapter :
    DataListRecyclerViewAdapter<InnerContenAccordionEmployment, EmploymentAdapter.ViewHolder>() {

    private var tmpPosition = 0
    private var isLayoutClickItem = false

    override fun onCreateDataViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(layout.adapter_employment))
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
            itemView.txt_title_accordion_employment.text = data.title
            itemView.txt_detail_accordion.text = data.content
            itemView.btn_accordion.setOnClickListener {
                if (itemView.txt_detail_accordion.visibility == View.GONE) {
                    itemView.txt_detail_accordion.visibility = View.VISIBLE
                } else {
                    itemView.txt_detail_accordion.visibility = View.GONE
                }
//                tmpPosition = adapterPosition
//                notifyDataSetChanged()
//                isLayoutClickItem = true
            }

//            if (isLayoutClickItem) {
//                if (tmpPosition == adapterPosition) {
//                    isLayoutClickItem = false
//                    itemView.txt_detail_accordion.visibility = View.VISIBLE
//                } else {
//                    itemView.txt_detail_accordion.visibility = View.GONE
//                }
//            } else {
//                itemView.txt_detail_accordion.visibility = View.GONE
//            }
        }
    }
}