package com.strategies360.extension.util

import android.net.Uri
import com.strategies360.mililani2.util.MyImagePicker
import itsmagic.present.imagepicker.ImagePicker

/** Allows quick listener setup */
fun MyImagePicker.setListener(onSuccess: OnImagePickerSuccess?, onFailure: OnImagePickerFailure?) {
    setListener(object : ImagePicker.OnImagePickerListener {
        override fun onImagePickerSuccess(resultUri: Uri?) {
            onSuccess?.invoke(resultUri)
        }

        override fun onImagePickerFailure() {
            onFailure?.invoke()
        }
    })
}

/** Allows quick listener setup */
fun MyImagePicker.Builder.withListener(onSuccess: OnImagePickerSuccess?, onFailure: OnImagePickerFailure?): MyImagePicker.Builder {
    return withListener(object : ImagePicker.OnImagePickerListener {
        override fun onImagePickerSuccess(resultUri: Uri?) {
            onSuccess?.invoke(resultUri)
        }

        override fun onImagePickerFailure() {
            onFailure?.invoke()
        }
    })
}

typealias OnImagePickerSuccess = ((resultUri : Uri?) -> Unit)
typealias OnImagePickerFailure = (() -> Unit)
