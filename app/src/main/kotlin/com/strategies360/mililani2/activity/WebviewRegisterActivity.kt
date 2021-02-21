package com.strategies360.mililani2.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import com.strategies360.mililani2.view.ProgressDialog
import kotlinx.android.synthetic.main.activity_webview_register.*
import kotlinx.android.synthetic.main.include_toolbar.*

class WebviewRegisterActivity : CoreActivity() {
  var prDialog: ProgressDialog? = null
  override val viewRes: Int
    get() = R.layout.activity_webview_register

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val url = getString(string.webview_url_register)
    prDialog = ProgressDialog(this)
    prDialog!!.setText(getString(string.loading))
    web_view.webViewClient = CustomWebClient(prDialog!!)
    web_view.settings.loadsImagesAutomatically = true
    web_view.settings.javaScriptEnabled = true
    web_view.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    web_view.loadUrl(url)

    title_toolbar.text = resources.getString(string.title_register)

    btn_back.setOnClickListener {
      onBackPressed()
    }

    btn_barcode.setOnClickListener {
      openBottomCardList()
    }
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager = supportFragmentManager
    MTACardBottomListFragment()
        .show(fragManager, "Dialog")
  }

  class CustomWebClient(var prDialog: ProgressDialog) : WebViewClient() {
    override fun shouldOverrideUrlLoading(
      view: WebView,
      url: String
    ): Boolean {
      view.loadUrl(url)
      return super.shouldOverrideUrlLoading(view, url)
    }

    override fun onPageFinished(
      view: WebView,
      url: String
    ) {
      super.onPageFinished(view, url)
      if (prDialog.isShowing) {
        prDialog.dismiss()
      }
    }

    init {
      prDialog.show()
    }
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, WebviewRegisterActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }
}