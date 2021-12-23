package com.strategies360.mililani2.view.spinner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.view.spinner.core.CoreTextInputSpinner
import com.strategies360.mililani2.view.spinner.core.CustomCoreTextInputSpinner
import java.text.SimpleDateFormat
import java.util.*

class KeywordEventTextInputSpinner : CustomCoreTextInputSpinner<String> {

  constructor(context: Context) : super(context)

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs)

  override fun onPrepare() {
    super.onPrepare()
    initSpinner()
  }

  private fun initSpinner() {
    setTextInputHint(context.getString(R.string.keyword_search_event))
    val listKeyword: ArrayList<String> = ArrayList()
    var data: String
    for (i in 0..1) {
      data = if (i == 0) {
        "Match One"
      } else {
        "Match All"
      }
      listKeyword.add(data)
    }
    setData(listKeyword)
    showPlaceholder()
  }
}
