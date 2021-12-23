package com.strategies360.mililani2.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.view.ProgressDialog
import kotlinx.android.synthetic.main.fragment_webview_classes.*
import kotlinx.android.synthetic.main.include_toolbar.*


class ClassesWebviewFragment : CoreFragment() {
    var prDialog: ProgressDialog? = null

    override val viewRes: Int
        get() = R.layout.fragment_webview_classes


    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        btn_back.setOnClickListener {
            BottomMenuNavigationActivity.launchIntent(requireContext())
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        prDialog = ProgressDialog(requireContext())
        prDialog!!.setText(getString(R.string.loading))

        webview_classes.settings.javaScriptEnabled = true
        webview_classes.settings.domStorageEnabled = true
        webview_classes.settings.javaScriptCanOpenWindowsAutomatically = true
        webview_classes.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview_classes.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val urlSample = request?.url.toString()
                view?.loadUrl("https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=AR")
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (pDialog != null) pDialog.visibility = View.GONE
                if (webview_classes != null) webview_classes.visibility = View.VISIBLE
                view?.scrollTo(0,0)
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                pDialog.visibility = View.GONE
                val errorMessage = "Got Error! $error"
                Common.showToast(errorMessage)
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                handler.proceed() // Ignore SSL certificate errors
            }
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }
        }

        webview_classes.loadUrl("https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=AR")
    }
}