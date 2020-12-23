package com.strategies360.mililani2.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.zxing.Result
import com.strategies360.mililani2.R
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.CustomViewFinderView
import kotlinx.android.synthetic.main.fragment_submit_scan_mta_card.btn_manually_mta_card
import kotlinx.android.synthetic.main.fragment_submit_scan_mta_card.frame_layout_camera
import kotlinx.android.synthetic.main.fragment_submit_scan_mta_card.txt_sample_barcode_result
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView

class SubmitScanMtaCardFragment : CoreFragment(), ZXingScannerView.ResultHandler, View.OnClickListener{

  private lateinit var mScannerView: ZXingScannerView

  override val viewRes = R.layout.fragment_submit_scan_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initBarcodeMTA()
    initDefaultView()
  }

  override fun onStart() {
    mScannerView.startCamera()
    doRequestPermission()
    super.onStart()
  }

  private fun doRequestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (context!!.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when (requestCode) {
      100 -> {
        initBarcodeMTA()
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }

  override fun onPause() {
    mScannerView.stopCamera()
    super.onPause()
  }

  private fun initBarcodeMTA() {
//    mScannerView = ZXingScannerView(context)
    mScannerView = object : ZXingScannerView(context) {
      override fun createViewFinderView(context: Context?): IViewFinder {
        return CustomViewFinderView(context!!)
      }
    }

    mScannerView.setAutoFocus(true)
    mScannerView.setResultHandler(this)
    frame_layout_camera.addView(mScannerView)
  }

  private fun initDefaultView() {
    txt_sample_barcode_result.text = "QR Code Value"
    btn_manually_mta_card.visibility = View.GONE
  }

  override fun onClick(v: View?) {
    when (view?.id) {
      R.id.btn_manually_mta_card -> {
        mScannerView.resumeCameraPreview(this)
        initDefaultView()
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }

  override fun handleResult(rawResult: Result?) {
    txt_sample_barcode_result.text = rawResult?.text
  }

}