package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.assessment.content.tab.accordion.DetailContentAccordions
import kotlinx.android.synthetic.main.adapter_form.view.*
import kotlinx.android.synthetic.main.fragment_forms.*

class FormAdapter :
    DataListRecyclerViewAdapter<DetailContentAccordions, FormAdapter.ViewHolder>() {

    private var tmpPosition = 0
    private var isLayoutClickItem = false

    override fun onCreateDataViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(layout.adapter_form))
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
            itemView.txt_title_accordion_form.text =  HtmlCompat.fromHtml(data.heading.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
            itemView.txt_detail_accordion.text = HtmlCompat.fromHtml(data.desc.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
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