package com.strategies360.mililani2.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.string
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import com.strategies360.mililani2.util.Common.showToast
import com.strategies360.mililani2.view.ProgressDialog
import kotlinx.android.synthetic.main.activity_webview_assessment.*
import kotlinx.android.synthetic.main.include_toolbar.*


class WebviewAssessmentActivity : CoreActivity() {
  var prDialog: ProgressDialog? = null
  override val viewRes: Int
    get() = R.layout.activity_webview_assessment

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val url = (this.intent.getStringExtra(getString(string.prefs_is_url_assessment)))

    prDialog = ProgressDialog(this)
    prDialog!!.setText(getString(string.loading))
//    web_view.webViewClient = CustomWebClient(prDialog!!)
    web_view.settings.javaScriptEnabled = true
    web_view.settings.domStorageEnabled = true
    web_view.settings.javaScriptCanOpenWindowsAutomatically = true
    web_view.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    web_view.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val urlSample = request?.url.toString()
        view?.loadUrl(urlSample)
        return super.shouldOverrideUrlLoading(view, request)
      }

      override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
      }

      override fun onPageFinished(view: WebView?, url: String?) {
        if (prDialog!!.isShowing) {
          prDialog!!.dismiss()
        }
        view?.scrollTo(0,0)
        super.onPageFinished(view, url)
      }

      override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        prDialog!!.dismiss()
        val errorMessage = "Got Error! $error"
        showToast(errorMessage)
        super.onReceivedError(view, request, error)
      }

      override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed()
      }

      override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        if (errorResponse != null) {
          Toast.makeText(this@WebviewAssessmentActivity,
            "WebView Error" + errorResponse.reasonPhrase,
            Toast.LENGTH_SHORT).show()
        }
      }
    }

    web_view.loadUrl(url)

    title_toolbar.text = " "

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
      view.scrollTo(0,0)
      super.onPageFinished(view, url)
      if (prDialog.isShowing) {
        prDialog.dismiss()
      }
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed() // Ignore SSL certificate errors
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
    fun launchIntent(context: Context, url: String) {
      val intent = Intent(context, WebviewAssessmentActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      intent.putExtra(context.getString(string.prefs_is_url_assessment),
              url)
      context.startActivity(intent)
    }
  }
}