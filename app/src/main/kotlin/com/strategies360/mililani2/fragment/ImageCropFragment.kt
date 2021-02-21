package com.strategies360.mililani2.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.strategies360.mililani2.R
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.local.util.ImageCropParams
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Debugger
import com.strategies360.mililani2.viewmodel.ImageCropViewModel
import itsmagic.present.imagepicker.util.ImagePickerUtil
import kotlinx.android.synthetic.main.fragment_image_crop.*

class ImageCropFragment : CoreFragment() {

    override val viewRes: Int? = R.layout.fragment_image_crop

    /** The view model for this fragment */
    private val viewModel by lazy { ViewModelProviders.of(this).get(ImageCropViewModel::class.java) }

    /** Flag to determine if image is ready to be edited */
    private var isEditorReady = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initExtras()
        beginEditing()
    }

    /** Initializes this fragment's viewmodel */
    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Resource.LOADING -> onSaveImageLoading()
                Resource.ERROR -> onSaveImageFailure(it.error)
                Resource.SUCCESS -> onSaveImageSuccess(it.data!!)
            }
        })
    }

    /** Initializes the data from passed arguments */
    private fun initExtras() {
        try {
            val extras = arguments as Bundle
            val params = ImageCropParams()

            var sourceUri: Uri? = extras.getParcelable(ImagePickerUtil.CROPPER_URI_SOURCE)
            var destinationUri: Uri? = extras.getParcelable(ImagePickerUtil.CROPPER_URI_DEST)
            if (sourceUri == null) sourceUri = Uri.EMPTY
            if (destinationUri == null) destinationUri = Uri.EMPTY
            params.sourceImage = sourceUri
            params.destinationImage = destinationUri
            params.aspectWidth = extras.getInt(ImagePickerUtil.CROPPER_RATIO_W)
            params.aspectHeight = extras.getInt(ImagePickerUtil.CROPPER_RATIO_H)
            params.minWidth = extras.getInt(ImagePickerUtil.CROPPER_MIN_W)
            params.minHeight = extras.getInt(ImagePickerUtil.CROPPER_MIN_H)
            params.maxWidth = extras.getInt(ImagePickerUtil.CROPPER_MAX_W)
            params.maxHeight = extras.getInt(ImagePickerUtil.CROPPER_MAX_H)

            viewModel.params = params
        } catch (e: Exception) {
            Debugger.logException(e)
        }
    }

    /**
     * Begin the editing process of the image.
     * Also sets the aspect ratio if it's specified.
     */
    private fun beginEditing() {
        // Set the source image to be cropped
        img_cropper.setOnSetImageUriCompleteListener { _, _, _ -> isEditorReady = true }
        img_cropper.setImageUriAsync(viewModel.params.sourceImage)

        // Set other stuff
        // TODO: Change background color if needed
        img_cropper.setBackgroundResource(android.R.color.darker_gray)

        // Set the aspect ratio if applicable
        if (viewModel.params.aspectWidth != -1 && viewModel.params.aspectHeight != -1) {
            img_cropper.setAspectRatio(viewModel.params.aspectWidth, viewModel.params.aspectHeight)
            img_cropper.setFixedAspectRatio(true)
        }
    }

    /** Finish the editing process of the image */
    fun finishEditing() {
        if (isEditorReady) viewModel.saveImage(img_cropper.croppedImage)
    }

    /** Rotate the image to be edited */
    fun rotateImage() {
        if (isEditorReady) img_cropper.rotateImage(90)
    }

    /** Show a progress dialog */
    private fun onSaveImageLoading() {
        activity?.let { Common.showProgressDialog(it) }
    }

    /** Finish the activity with a RESULT_OK */
    private fun onSaveImageSuccess(imageUri: Uri) {
        activity?.let {
            Common.dismissProgressDialog()

            val intent = Intent()
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    /** Show an error message dialog */
    private fun onSaveImageFailure(error: AppError) {
        activity?.let {
            Common.dismissProgressDialog()
            Common.showMessageDialog(it, "Error", error.message)
        }
    }
}
