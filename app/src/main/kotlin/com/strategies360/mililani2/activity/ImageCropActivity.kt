package com.strategies360.mililani2.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.ImageCropFragment
import kotlinx.android.synthetic.main.activity_image_crop.*

/**
 *
 * Image cropping activity.
 */
class ImageCropActivity : CoreActivity() {

    override val viewRes = R.layout.activity_image_crop

    private val fragment = ImageCropFragment()

    private var btnRotate: MenuItem? = null
    private var btnSave: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        initFragment()
    }

    /** Initialize the fragment */
    private fun initFragment() {
        fragment.arguments = intent.extras
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layout_fragment.id, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_image_crop, menu)
        btnRotate = menu.findItem(R.id.action_crop_rotate)
        btnSave = menu.findItem(R.id.action_crop_done)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_crop_rotate -> {
                fragment.rotateImage()
                return true
            }
            R.id.action_crop_done -> {
                fragment.finishEditing()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
