package com.strategies360.mililani2.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.SampleAuthActivity
import com.strategies360.mililani2.activity.SampleProductListActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.MyImagePicker
import com.strategies360.extension.util.withListener
import kotlinx.android.synthetic.main.fragment_sample_selection.*

/**
 *
 * Sample fragment to open another sample fragments.
 */
class SampleSelectionFragment : CoreFragment() {

    override val viewRes = R.layout.fragment_sample_selection

    // TODO: Remove these useless vars
    var selectedUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_sample_api.text = getString(R.string.flavor_api_sample)
//        txt_sample_mode.text = getString(R.string.mo)

        btn_datalist.setOnClickListener { SampleProductListActivity.launchIntent(context!!) }
        btn_authentication.setOnClickListener { SampleAuthActivity.launchIntent(context!!) }
        btn_image_picker.setOnClickListener {
            val builder = MyImagePicker.Builder(activity!!)
            builder.withCrop(true)
            if (selectedUri != null) {
                builder.withImageRemoveListener {
                    selectedUri = null
                    Common.showToast("Removed previously selected image")
                }
            }
            builder.withListener(
                    onSuccess = { resultUri ->
                        selectedUri = resultUri
                        Common.showToast("Uri = $selectedUri")
                    },
                    onFailure = {
                        Common.showToast("Failed to pick image")
                    }
            )
            builder.showPicker()
        }
        btn_settings_account.setOnClickListener { startActivity(Intent(Settings.ACTION_SYNC_SETTINGS)) }
        btn_force_close.setOnClickListener { throw RuntimeException("This is a forced crash") }

        spinner_sample.setTextInputHint("HINT")
//        spinner_sample.setTextInputPlaceholder("PLACEHOLDER")
//        spinner_sample.setTextInputError("ERROR")

        spinner_sample_product.setTextInputHint("HINT")
//        spinner_sample_product.setTextInputPlaceholder("PLACEHOLDER")
//        spinner_sample_product.setTextInputError("ERROR")
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SampleSelectionFragment::class.java)
            context.startActivity(intent)
        }
    }
}
