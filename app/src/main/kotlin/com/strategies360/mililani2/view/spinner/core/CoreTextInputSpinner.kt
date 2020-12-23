package com.strategies360.mililani2.view.spinner.core

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.CallSuper
import com.strategies360.mililani2.adapter.spinner.TextInputSpinnerAdapter
import itsmagic.present.easierspinner.RefreshableEasierSpinnerCore

abstract class CoreTextInputSpinner<DATA_TYPE> : RefreshableEasierSpinnerCore<DATA_TYPE> {

    lateinit var adapter: TextInputSpinnerAdapter<DATA_TYPE>

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @CallSuper
    override fun onPrepare() {
        removeSpinnerBackground()
        initAdapter()
    }

    private fun initAdapter() {
        adapter = TextInputSpinnerAdapter(context)
        setAdapter(adapter)
    }

    @Deprecated("Don't use this when using this spinner class", ReplaceWith("setTextInputPlaceholder(string)"))
    override fun setPlaceholder(placeholder: DATA_TYPE) {
        super.setPlaceholder(placeholder)
    }

    fun setTextInputHint(hint: String?) {
        adapter.textInputHintText = hint
    }

    fun setTextInputPlaceholder(placeholder: String?) {
        adapter.textInputPlaceholderText = placeholder
    }

    fun setTextInputError(error: String?) {
        adapter.textInputErrorText = error
    }

    fun setTextInputHelper(helper: String?) {
        adapter.textInputHelperText = helper
    }
}
