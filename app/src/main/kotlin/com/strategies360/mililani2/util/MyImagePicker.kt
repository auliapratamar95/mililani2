package com.strategies360.mililani2.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.ImageCropActivity
import itsmagic.present.imagepicker.ImagePicker
import itsmagic.present.permissionhelper.util.PermissionHelper
import kotlinx.android.synthetic.main.dialog_imagepicker.view.*
import java.io.File
import java.io.IOException

class MyImagePicker : ImagePicker {

    /** Determines if image can be removed by displaying an additional option on the dialog  */
    private var isRemovable = false

    /** The listener for image removal  */
    var onImageRemoved: OnImageRemoved? = null
        set(value) {
            isRemovable = value != null

            // Re-set the dialog to add/remove the remove image button
            setCustomDialog(initCustomDialog())
            setCustomNoCameraDialog(initCustomNoCameraDialog())

            field = value
        }

    /**
     * The constructor of this class.
     * @param activity The activity
     */
    constructor(activity: Activity) : super(activity)

    /**
     * The constructor of this class.
     * @param builder The builder object
     */
    constructor(builder: MyImagePicker.Builder) : super(builder) {
        onImageRemoved = builder.onImageRemoved
    }

    override fun onInit() {
        setDirectory(initCustomDirectory())
        setCroppingClass(initCroppingClass())
        setCustomDialog(initCustomDialog())
        setCustomNoCameraDialog(initCustomNoCameraDialog())
    }

    /**
     * Initialize a custom directory for this image picker
     *
     * Obtain the external storage's picture folder path.
     * Also generates a '.nomedia' file if it doesn't exist.
     * @return The directory as a [File]
     */
    private fun initCustomDirectory(): File {
        val appName = activity.resources.getString(activity.applicationInfo.labelRes)
        val storageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appName)

        // Create the directories if needed
        if (!storageDir.exists()) storageDir.mkdirs()

        // Generate a '.nomedia' file
        val nomediaFile = File("$storageDir/.nomedia")
        if (!nomediaFile.exists()) {
            try {
                nomediaFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return storageDir
    }

    /** Initialize a custom cropping class for this image picker  */
    private fun initCroppingClass(): Class<*> {
        return ImageCropActivity::class.java
    }

    /** Initialize a custom dialog for this image picker  */
    private fun initCustomDialog(): Dialog {
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(initPickerView(dialog))
        return dialog
    }

    /** Initialize a custom dialog for this image picker for devices without camera  */
    private fun initCustomNoCameraDialog(): Dialog? {
        return if (isRemovable)
            null
        else {
            val dialog = BottomSheetDialog(activity)
            val view = initPickerView(dialog)
            view.btn_picker_camera.visibility = View.GONE
            dialog.setContentView(view)
            dialog
        }
    }

    @SuppressLint("InflateParams")
    private fun initPickerView(dialog: Dialog): View {
        // TODO: Change layout accordingly
        val view = activity.layoutInflater.inflate(R.layout.dialog_imagepicker, null)

        // Camera button
        view.btn_picker_camera.setOnClickListener {
            dialog.dismiss()
            onCameraSelection()
        }

        // Gallery button
        view.btn_picker_gallery.setOnClickListener {
            dialog.dismiss()
            onPickerSelection()
        }

        // Remove button
        if (isRemovable) {
            view.btn_picker_remove.visibility = View.VISIBLE
            view.btn_picker_remove.setOnClickListener {
                dialog.dismiss()
                onImageRemoved?.invoke()
            }
        } else {
            view.btn_picker_remove.visibility = View.GONE
        }

        return view
    }

    override fun onCameraSelection() {
        PermissionHelper.Builder(activity)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onPermissionGranted { _, _ -> callSuperCameraIntent() }
                .onPermissionDenied { _, _, isCompletelyDenied -> showDeniedDialog(isCompletelyDenied) }
                .requestPermission()
    }

    override fun onPickerSelection() {
        PermissionHelper.Builder(activity)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onPermissionGranted { _, _ -> callSuperPickerIntent() }
                .onPermissionDenied { _, _, isCompletelyDenied -> showDeniedDialog(isCompletelyDenied) }
                .requestPermission()
    }

    /** Show the permission denied dialog */
    private fun showDeniedDialog(isCompletelyDenied: Boolean) {
        // TODO: Use own implementation when permission is denied
        val builder = AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Alert)
        builder.setTitle(R.string.error_permission_denied)
        if (!isCompletelyDenied) {
            builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        } else {
            builder.setPositiveButton(R.string.action_open_app_settings) { dialog, _ ->
                dialog.dismiss()
                PermissionHelper.startSettingsActivity(activity, activity.packageName)
            }
        }
        builder.show()
    }

    /** Call the parent class' camera selection  */
    private fun callSuperCameraIntent() {
        super.onCameraSelection()
    }

    /** Call the parent class' picker selection  */
    private fun callSuperPickerIntent() {
        super.onPickerSelection()
    }

    /**
     * The image picker builder
     * @param activity The activity
     */
    class Builder(activity: Activity) : ImagePicker.CustomBuilder<Builder>(activity) {

        var onImageRemoved: OnImageRemoved? = null
            private set

        /**
         * Determines if image can be removed by displaying an additional option on the dialog
         * @param action when not set as null, the picker will display an option to remove the image
         */
        fun withImageRemoveListener(action: OnImageRemoved?): Builder {
            onImageRemoved = action
            return this
        }

        override fun build(): MyImagePicker {
            return MyImagePicker(this)
        }

        override fun showPicker() {
            MyImagePicker(this).showPicker()
        }
    }
}

typealias OnImageRemoved = (() -> Unit)
