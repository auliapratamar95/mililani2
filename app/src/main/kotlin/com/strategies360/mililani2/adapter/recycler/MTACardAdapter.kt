package com.strategies360.mililani2.adapter.recycler

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.RGB_565
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat.CODE_128
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.adapter.recycler.MTACardAdapter.ViewHolder
import com.strategies360.mililani2.adapter.recycler.core.DataListRecyclerViewAdapter
import com.strategies360.mililani2.eventbus.EventDefaultCardNumber
import com.strategies360.mililani2.eventbus.EventPrimaryCardNumber
import com.strategies360.mililani2.model.remote.mtaCard.MTACard
import kotlinx.android.synthetic.main.adapter_recycler_mta_card_product.view.*
import org.greenrobot.eventbus.EventBus

class MTACardAdapter : DataListRecyclerViewAdapter<MTACard, ViewHolder>() {

  private var count = 0
  private var itemWidth = 0

  override fun onCreateDataViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(parent.inflate(layout.adapter_recycler_mta_card_product))
  }

  override fun onBindDataViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bindView()
  }

  private fun convertCodeBarcode(
    itemView: View,
    codeMtaCard: String?
  ) {
    val multiFormatWriter = MultiFormatWriter()
    try {
      val bitMatrix = multiFormatWriter.encode(
          codeMtaCard, CODE_128, 250, 50
      )
      val bitmap: Bitmap = Bitmap.createBitmap(
          250, 50, RGB_565
      )
      for (i in 0 until 250) {
        for (j in 0 until 50) {
          bitmap.setPixel(i, j, if (bitMatrix[i, j]) Color.BLACK else Color.WHITE)
        }
      }
      itemView.barcode_mta_card.setImageBitmap(bitmap)
    } catch (e: WriterException) {
      e.printStackTrace()
    }
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bindView() {
      val data = getDataList()[adapterPosition]

      if (data.nickname != null) itemView.txt_nickname.setText(
          data.nickname
      ) else itemView.txt_nickname.setText(
          ""
      )

      convertCodeBarcode(itemView, data.cardNumber)

      if (data.isDefault == 0) {
        itemView.sample.gravity = Gravity.LEFT
        itemView.btn_set_primary.visibility = View.VISIBLE
      } else {
        itemView.sample.gravity = Gravity.CENTER
        itemView.txt_nickname.setTextColor(App.context.getColor(R.color.primary_card))
        itemView.btn_set_primary.visibility = View.GONE
      }

      itemView.txt_card_number.text = data.cardNumber

      itemView.btn_set_primary.setOnClickListener {
        EventBus.getDefault().postSticky(EventDefaultCardNumber(data.cardNumber.toString()))
      }

      itemView.txt_nickname.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          if (itemView.txt_nickname.text != null && itemView.txt_nickname.text!!.isNotEmpty()) {
            EventBus.getDefault().postSticky(
                EventPrimaryCardNumber(
                    data.cardNumber.toString(), itemView.txt_nickname.text.toString()
                )
            )
          } else {
            Toast
                .makeText(
                    App.context,
                    "Please type your nickname",
                    Toast.LENGTH_SHORT
                )
                .show()
          }
          true
        } else {
          false
        }
      }
      itemView.setOnClickListener {}
    }
  }
}