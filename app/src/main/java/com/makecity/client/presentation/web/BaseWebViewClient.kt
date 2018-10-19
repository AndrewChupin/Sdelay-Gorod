package com.makecity.client.presentation.web

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


interface WebViewDelegate {
	fun onPageFinished(view: WebView, url: String)
	fun onReceivedError(view: WebView, request: WebResourceRequest,
						error: WebResourceError)

	fun onReceivedErrorOld(view: WebView, errorCode: Int, description: String,
						   failingUrl: String)
}


class BaseWebViewClient(private val webViewDelegate: WebViewDelegate) : WebViewClient() {

	override fun onPageFinished(view: WebView, url: String) {
		super.onPageFinished(view, url)
		webViewDelegate.onPageFinished(view, url)
	}

	@TargetApi(Build.VERSION_CODES.M)
	override fun onReceivedError(view: WebView,
								 request: WebResourceRequest,
								 error: WebResourceError) {
		super.onReceivedError(view, request, error)
		webViewDelegate.onReceivedError(view, request, error)
	}

	override fun onReceivedError(view: WebView, errorCode: Int, description: String,
								 failingUrl: String) {
		super.onReceivedError(view, errorCode, description, failingUrl)
		webViewDelegate.onReceivedErrorOld(view, errorCode, description, failingUrl)
	}
}