package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.model.remote.product.SampleProduct
import com.strategies360.extension.android.view.inflate
import kotlinx.android.synthetic.main.adapter_recycler_sample_product.view.*

/**
 *
 * Sample adapter for sample product.
 */
class SampleProductAdapter : DataListRecyclerViewAdapter<SampleProduct, SampleProductAdapter.ViewHolder>() {

    var onProductClick: OnProductClick? = null

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_recycler_sample_product))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val data = getDataList()[adapterPosition]
            itemView.txt_sample.text = "Position: [$adapterPosition]\nItem: $data"
            itemView.setOnClickListener { onProductClick?.invoke(data, adapterPosition) }
        }
    }
}

typealias OnProductClick = ((data: SampleProduct, position: Int) -> Unit)
