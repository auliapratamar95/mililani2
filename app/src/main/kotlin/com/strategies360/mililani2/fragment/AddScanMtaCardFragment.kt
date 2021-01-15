package com.strategies360.mililani2.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.zxing.Result
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.SubmitFinishMtaCardActivity
import com.strategies360.mililani2.activity.SubmitManuallyMtaCardActivity
import com.strategies360.mililani2.activity.SubmitNicknameMtaCardActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.CustomViewFinderView
import com.strategies360.mililani2.viewmodel.SubmitMTACardViewModel
import kotlinx.android.synthetic.main.fragment_submit_scan_mta_card.btn_manually_mta_card
import kotlinx.android.synthetic.main.fragment_submit_scan_mta_card.frame_layout_camera
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView

class AddScanMtaCardFragment : CoreFragment(),
    ZXingScannerView.ResultHandler,
    View.OnClickListener {

  private lateinit var mScannerView: ZXingScannerView

  private var code: String = ""
  private var isBottomCardList: Boolean = false

  /** The view model for sign in */
  private val submitMTACardViewModel by lazy {
    ViewModelProviders.of(this)
        .get(SubmitMTACardViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_add_scan_mta_card

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()
    initBarcodeMTA()

    btn_manually_mta_card.setOnClickListener(this)
  }

  override fun onStart() {
    mScannerView.startCamera()
    mScannerView.setAutoFocus(true)
    doRequestPermission()
    super.onStart()
  }

  private fun doRequestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (requireContext().checkSelfPermission(
              Manifest.permission.CAMERA
          ) != PackageManager.PERMISSION_GRANTED
      ) {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
      }
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      100 -> {
        mScannerView.startCamera()
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
    mScannerView = object : ZXingScannerView(context) {
      override fun createViewFinderView(context: Context?): IViewFinder {
        return CustomViewFinderView(context!!)
      }
    }

    mScannerView.setAutoFocus(true)
    mScannerView.setResultHandler(this)
    frame_layout_camera.addView(mScannerView)
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.btn_manually_mta_card -> {
        mScannerView.resumeCameraPreview(this)
        SubmitManuallyMtaCardActivity.launchIntent(requireContext(), true)
      }
      else -> {
        /* nothing to do in here */
      }
    }
  }

  override fun handleResult(rawResult: Result?) {
    try {
      val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
      val r = RingtoneManager.getRingtone(context, notification)
      r.play()
      code = rawResult?.text.toString()

      val mtaCardRequest = MTACardRequest()
      r.play()
      code = rawResult?.text.toString()

      mtaCardRequest.cardNumber = code
      submitMTACard(mtaCardRequest)
    } catch (e: Exception) {

    }

  }

  private fun initViewModel() {
    submitMTACardViewModel.liveData.observe(viewLifecycleOwner, Observer {
      when (it?.status) {
        Resource.LOADING -> onSubmitMTACardLoading()
        Resource.ERROR -> onSubmitMTACardFailure(it.error)
        Resource.SUCCESS -> onSubmitMTACardSuccess(it.data!!)
      }
    })
  }

  /** Load user's profile from a remote server (async)  */
  private fun submitMTACard(data: MTACardRequest) {
    submitMTACardViewModel.submitInMililani(data)
  }

  private fun onSubmitMTACardLoading() {
    activity?.let {
      Common.showProgressDialog(it, onBackPress = {
        submitMTACardViewModel.cancelSubmitMTACArd()
        Common.dismissProgressDialog()
      })
    }
  }

  private fun onSubmitMTACardSuccess(response: SignInMililaniResponse) {
    Common.dismissProgressDialog()
    if (response.data != null) {
      SubmitNicknameMtaCardActivity.launchIntent(requireContext(), code)
      requireActivity().finish()
    }
  }

  private fun onSubmitMTACardFailure(error: AppError) {
    activity?.let {
      Common.dismissProgressDialog()
      if (error.code == 1000) {
        SubmitFinishMtaCardActivity.launchIntent(requireContext(), code)
        requireActivity().finishAffinity()
      } else
        Common.showMessageDialog(it, "Error", error.message)
    }
  }
}