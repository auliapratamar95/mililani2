package com.strategies360.mililani2.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.local.util.ImageCropParams
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageCropViewModel : ViewModel() {

    /** The LiveData for cropped image uri  */
    val liveData = MutableLiveData<Resource<Uri>>()

    /** The image cropping params */
    var params = ImageCropParams()
        set(params) {
            field = params
            isMinValid = false
            isMaxValid = false

            if (params.minWidth != -1 && params.minHeight != -1) isMinValid = true
            if (params.maxWidth != -1 && params.maxHeight != -1) isMaxValid = true
            if (isMinValid && isMaxValid) {
                if (params.minWidth > params.maxWidth)
                    throw IllegalArgumentException("Minimum width is larger than maximum width!")
                else if (params.minHeight > params.maxHeight) throw IllegalArgumentException("Minimum height is larger than maximum height!")
            }
        }

    private var isMinValid = false
    private var isMaxValid = false

    /**
     * Save an image to storage
     * @param image The image to save
     */
    fun saveImage(image: Bitmap) {
        liveData.value = Resource.loading()

        var newImage = image
        var isEdited = false

        // Resize the image if applicable
        if (isMinValid || isMaxValid) {
            var width = image.width
            var height = image.height

            if (!isEdited && isMinValid) {
                if (width < params.minWidth || height < params.minHeight) {
                    if (width > height) {
                        // landscape
                        val ratio = width.toFloat() / params.minWidth
                        width = params.minWidth
                        height = (height / ratio).toInt()
                    } else if (height > width) {
                        // portrait
                        val ratio = height.toFloat() / params.minHeight
                        width = (width / ratio).toInt()
                        height = params.minHeight
                    } else {
                        // square
                        width = params.minWidth
                        height = params.minHeight
                    }

                    newImage = Bitmap.createScaledBitmap(image, width, height, false)
                    isEdited = true
                }
            }

            if (!isEdited && isMaxValid) {
                if (width > params.maxWidth || height > params.maxHeight) {
                    if (width > height) {
                        // landscape
                        val ratio = width.toFloat() / params.maxWidth
                        width = params.maxWidth
                        height = (height / ratio).toInt()
                    } else if (height > width) {
                        // portrait
                        val ratio = height.toFloat() / params.maxHeight
                        width = (width / ratio).toInt()
                        height = params.maxHeight
                    } else {
                        // square
                        width = params.maxWidth
                        height = params.maxHeight
                    }

                    newImage = Bitmap.createScaledBitmap(image, width, height, false)
                    isEdited = true
                }
            }
        }

        try {
            val file = File(params.destinationImage.path!!)
            val out = FileOutputStream(file)

            // TODO: Change the image compression
            newImage.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

            liveData.value = Resource.success(params.destinationImage)
        } catch (e: IOException) {
            liveData.value = Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, e.message))
        }
    }
}
