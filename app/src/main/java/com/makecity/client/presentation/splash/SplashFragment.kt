package com.makecity.client.presentation.splash

import android.os.Bundle
import android.view.View
import com.makecity.client.R
import com.makecity.client.app.AppInjector
import com.makecity.core.extenstion.hideWithScale
import com.makecity.core.extenstion.isVisible
import com.makecity.core.extenstion.showWithScale
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.view.StatementFragment
import kotlinx.android.synthetic.main.fragment_splash.*


// Statement
typealias SplashStatement = StatementFragment<SplashReducer, SplashDataViewState, SplashAction>


// Fragment
class SplashFragment: SplashStatement() {

	companion object {
		fun newInstance() = SplashFragment()
	}

	override val layoutId: Int = R.layout.fragment_splash

	override fun onInject() = AppInjector.inject(this)

	override fun onViewCreatedBeforeRender(savedInstanceState: Bundle?) {
		button_retry.setOnClickListener { changeState() }
	}

	override fun render(state: SplashDataViewState) = when (state.screenState) {
		is PrimaryViewState.Loading -> applyConnectionState(state.connectionState)
		is PrimaryViewState.Error -> {
			showMessage(state.screenState.error)
			changeState(isVisibleRetry = true)
		}
		else -> Unit
	}

	private fun applyConnectionState(connectionState: ConnectionState) = when (connectionState) {
		is ConnectionState.Connect -> {
			val isWaitingConnection = linear_connection_loading.isVisible
			if (isWaitingConnection) {
				reducer.reduce(SplashAction.PrepareData)
			}
			changeState(isVisibleLoading = false)
		}
		is ConnectionState.Disconnect -> changeState(isVisibleLoading = true)
		is ConnectionState.Unknown -> Unit
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		reducer.reduce(SplashAction.PrepareData)
	}

	private fun changeState(isVisibleLoading: Boolean = false, isVisibleRetry: Boolean = false) {
		linear_connection_loading.isVisible = isVisibleLoading
		if (isVisibleRetry) button_retry.showWithScale() else button_retry.hideWithScale()
	}
}
