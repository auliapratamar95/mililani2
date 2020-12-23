package com.strategies360.mililani2.view.spinner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.strategies360.mililani2.model.remote.product.SampleProduct
import com.strategies360.mililani2.view.spinner.core.CoreTextInputSpinner
import java.util.*

class SampleProductTextInputSpinner : CoreTextInputSpinner<SampleProduct> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onPrepare() {
        super.onPrepare()
        initSpinner()
    }

    private fun initSpinner() {
        // Generate the adapter data
        val simpleData = ArrayList<SampleProduct>()
        var data: SampleProduct
        val rnd = Random()
        var color: Int
        for (i in 0..9) {
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            data = SampleProduct(i)
            data.name = String.format("#%06X", 0xFFFFFF and color)
            simpleData.add(data)
        }
        setData(simpleData)
        showPlaceholder()
    }
}
