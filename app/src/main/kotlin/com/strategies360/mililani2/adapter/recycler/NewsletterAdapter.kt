package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.newsletter.Newsletter
import kotlinx.android.synthetic.main.adapter_newsletter.view.*


class NewsletterAdapter : DataListRecyclerViewAdapter<Newsletter, NewsletterAdapter.ViewHolder>() {

    private var tmpPosition = 0
    private var isBackground = false

    var onItemNewsClick: onItemNewsClick? = null

    override fun onCreateDataViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(parent.inflate(layout.adapter_newsletter))
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
            isBackground = if (!isBackground) {
                itemView.layout_card_newsletter.setCardBackgroundColor(App.context.getColor(R.color.grey_newsletter))
                true
            } else {
                itemView.layout_card_newsletter.setCardBackgroundColor(App.context.getColor(R.color.white))
                false
            }
            itemView.txtTitleNewsletter.text = data.postName
            itemView.img_archive.setOnClickListener {
                onItemNewsClick?.invoke(adapterPosition, data)
            }
            itemView.btn_download.setOnClickListener {
                onItemNewsClick?.invoke(adapterPosition, data)
            }
        }
    }
}

typealias onItemNewsClick = ((position: Int, data: Newsletter) -> Unit)
