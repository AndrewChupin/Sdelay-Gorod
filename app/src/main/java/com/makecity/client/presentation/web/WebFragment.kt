package com.makecity.client.presentation.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.Toolbar
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.withArguments
import com.makecity.core.presentation.screen.ToolbarConfig
import com.makecity.core.presentation.screen.ToolbarScreen
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.toolbar.*


@Parcelize
data class WebData(
	val url: String,
	val title: String
) : Parcelable


typealias WebStatement = StatementFragment<WebReducer, WebViewState, WebAction>


class WebFragment : WebStatement(), ToolbarScreen, WebViewDelegate {

	companion object {
		private const val ARGUMENT_WEB_DATA = "ARGUMENT_WEB_DATA"

		fun newInstance(webData: WebData) = WebFragment().withArguments {
			putParcelable(ARGUMENT_WEB_DATA, webData)
		}
	}

	override val layoutId: Int = R.layout.fragment_web

	override fun onInject() = AppInjector.inject(this, getArgument(ARGUMENT_WEB_DATA))

	override fun getToolbar(): Toolbar = toolbar

	@SuppressLint("SetJavaScriptEnabled")
	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		setupToolbarWith(requireActivity(), ToolbarConfig(title = EMPTY))

		web_content_view.apply {
			settings.apply {
				useWideViewPort = true
				javaScriptEnabled = true
				setAppCacheEnabled(true)

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
				}
			}

			setDownloadListener { url, _, _, _, _ ->
				val intent = Intent(Intent.ACTION_VIEW)
				intent.data = Uri.parse(url)
				startActivity(intent)
			}

			webViewClient = BaseWebViewClient(this@WebFragment)
		}
	}

	override fun render(state: WebViewState) {
		toolbar.title = state.webData.title

		when (state.screenState) {
			is PrimaryViewState.Data -> {
				web_content_view.isVisible = true
				web_progress.isVisible = false
			}
			is PrimaryViewState.Loading -> {
				web_progress.isVisible = true
				web_content_view.isVisible = false
				web_content_view.loadUrl(state.webData.url)
			}
			is PrimaryViewState.Error -> {
				web_progress.isVisible = false
				web_content_view.isVisible = false
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		web_content_view.saveState(outState)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		web_content_view.restoreState(savedInstanceState)
	}

	override fun onBackClick(): Boolean {
		if (web_content_view.canGoBack()) {
			web_content_view.goBack()
			return true
		}

		reducer.reduce(WebAction.Exit)
		return true
	}


	// IMPLEMENT - WebViewDelegate
	override fun onPageFinished(view: WebView, url: String) = reducer.reduce(WebAction.Success)

	override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) = reducer.reduce(WebAction.Error)

	override fun onReceivedErrorOld(view: WebView, errorCode: Int, description: String, failingUrl: String) = reducer.reduce(WebAction.Error)
}