package com.strategies360.mililani2.adapter.spinner

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.strategies360.extension.android.view.inflate
import com.strategies360.mililani2.R
import itsmagic.present.easierspinner.adapter.EasierSpinnerAdapterCore
import itsmagic.present.easierspinner.adapter.EasierSpinnerStringAdapter
import itsmagic.present.easierspinner.adapter.EasierSpinnerViewHolder
import kotlinx.android.synthetic.main.adapter_spinner_textinput.view.*

class TextInputSpinnerAdapter<DATA_TYPE>(context: Context) : EasierSpinnerAdapterCore<DATA_TYPE, TextInputSpinnerAdapter<DATA_TYPE>.ViewHolder, TextInputSpinnerAdapter<DATA_TYPE>.ViewHolder, TextInputSpinnerAdapter<DATA_TYPE>.DropDownViewHolder>(context) {

    /** The hint text  */
    var textInputHintText: String? = null
        set(value) {
            val isChanged = (field != value)
            field = value
            if (isChanged) notifyDataSetChanged()
        }

    /** The placeholder text  */
    var textInputPlaceholderText: String? = null
        set(value) {
            val isChanged = (field != value)
            field = value
            if (isChanged) notifyDataSetChanged()
        }

    /** The error text  */
    var textInputErrorText: String? = null
        set(value) {
            val isChanged = (field != value)
            field = value
            if (isChanged) notifyDataSetChanged()
        }

    /** The helper text  */
    var textInputHelperText: String? = null
        set(value) {
            val isChanged = (field != value)
            field = value
            if (isChanged) notifyDataSetChanged()
        }

    override fun onCreatePlaceholderViewHolder(parent: ViewGroup): ViewHolder {
        val view = parent.inflate(R.layout.adapter_spinner_textinput)
        return ViewHolder(view)
    }

    override fun onCreateSelectionViewHolder(parent: ViewGroup): ViewHolder {
        val view = parent.inflate(R.layout.adapter_spinner_textinput)
        return ViewHolder(view)
    }

    override fun onCreateDropdownViewHolder(parent: ViewGroup): DropDownViewHolder {
        val view = parent.inflate(android.R.layout.simple_spinner_dropdown_item)
        return DropDownViewHolder(view)
    }

    override fun onBindPlaceholderViewHolder(holder: ViewHolder, placeholder: DATA_TYPE) {
        holder.bindView(placeholder, true)
    }

    override fun onBindSelectionViewHolder(holder: ViewHolder, position: Int, data: DATA_TYPE?) {
        holder.bindView(data, false)
    }

    override fun onBindDropdownViewHolder(holder: DropDownViewHolder, position: Int, data: DATA_TYPE?, isPlaceholder: Boolean) {
        holder.bindView(data, isPlaceholder)
    }

    inner class ViewHolder(itemView: View) : EasierSpinnerViewHolder(itemView) {

        init {
            itemView.apply {
                txt_input.setOnClickListener { easierSpinner.showSelection() }
            }
        }

        fun bindView(data: DATA_TYPE?, isPlaceholder: Boolean) {
            itemView.apply {
                txt_input.setText(
                        if (isPlaceholder) {
                            textInputPlaceholderText
                        } else {
                            if (data != null) when (data) {
                                is EasierSpinnerStringAdapter.SpinnerText -> data.spinnerText
                                is CharSequence -> data
                                else -> data.toString()
                            }
                            else null
                        })
                txt_input_layout.apply {
                    hint = textInputHintText
                    isHintEnabled = !textInputHintText.isNullOrBlank()

                    helperText = textInputHelperText
                    isHelperTextEnabled = !textInputHelperText.isNullOrBlank()

                    error = textInputErrorText
                    isErrorEnabled = !textInputErrorText.isNullOrBlank()
                }
            }
        }
    }

    inner class DropDownViewHolder(itemView: View) : EasierSpinnerViewHolder(itemView) {

        fun bindView(data: DATA_TYPE?, isPlaceholder: Boolean) {
            (itemView as TextView).text =
                    if (isPlaceholder) {
                        textInputPlaceholderText
                    } else {
                        if (data != null) when (data) {
                            is EasierSpinnerStringAdapter.SpinnerText -> data.spinnerText
                            is CharSequence -> data
                            else -> data.toString()
                        }
                        else null
                    }
        }
    }
}
