package com.strategies360.mililani2.adapter.viewpager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.RGB_565
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.google.zxing.BarcodeFormat.CODE_128
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.strategies360.mililani2.App
import com.strategies360.mililani2.R
import com.strategies360.mililani2.eventbus.EventDefaultCardNumber
import com.strategies360.mililani2.eventbus.EventPrimaryCardNumber
import com.strategies360.mililani2.model.remote.mtaCard.MTACard
import kotlinx.android.synthetic.main.adapter_custom_card_list.view.*
import org.greenrobot.eventbus.EventBus

class ViewPagerKotlinAdapter: PagerAdapter() {

  private var models: ArrayList<MTACard> = ArrayList()
  private var context: Context? = null

  fun viewPagerKotlinAdapter(
    models: ArrayList<MTACard>,
    context: Context?
  ) {
    this.models = models
    this.context = context
  }

  override fun getCount(): Int {
    return models.size
  }

  override fun isViewFromObject(
    view: View,
    `object`: Any
  ): Boolean {
    return view == `object`
  }

  override fun instantiateItem(
    container: ViewGroup,
    position: Int
  ): Any {
    val item = LayoutInflater.from(container.context).inflate(R.layout.adapter_custom_card_list, container, false)

    if (models[position].nickname != null) {
      item.txt_nickname.setText(
          models[position].nickname
      )
    } else item.txt_nickname.setText(
        ""
    )

    convertCodeBarcode(item, models[position].cardNumber)
    
    if (models[position].isDefault == 0) {
      item.sample.gravity = Gravity.LEFT
      item.btn_set_primary.visibility = View.VISIBLE
    } else {
      item.sample.gravity = Gravity.CENTER
      item.btn_set_primary.visibility = View.GONE
    }

    item.txt_card_number.text = models[position].cardNumber

    item.btn_set_primary.setOnClickListener {
      EventBus.getDefault().postSticky(EventDefaultCardNumber(models[position].cardNumber.toString()))
    }

    item.txt_nickname.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (item.txt_nickname.text != null && item.txt_nickname.text!!.isNotEmpty()) {
          EventBus.getDefault().postSticky(
              EventPrimaryCardNumber(
                  models[position].cardNumber.toString(), item.txt_nickname.text.toString()
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

    container.addView(item)
    return item
  }

  private fun convertCodeBarcode(
    item: View,
    codeMtaCard: String?
  ) {
    val multiFormatWriter = MultiFormatWriter()
    try {
      val bitMatrix = multiFormatWriter.encode(
          codeMtaCard, CODE_128, 1250, 500
      )
      val bitmap: Bitmap = Bitmap.createBitmap(
          1250, 500, RGB_565
      )
      for (i in 0 until 1250) {
        for (j in 0 until 500) {
          bitmap.setPixel(i, j, if (bitMatrix[i, j]) Color.BLACK else Color.WHITE)
        }
      }
      item.barcode_mta_card.setImageBitmap(bitmap)
    } catch (e: WriterException) {
      e.printStackTrace()
    }
  }

  override fun destroyItem(
    container: ViewGroup,
    position: Int,
    `object`: Any
  ) {
    container.removeView(`object` as View)
  }
}