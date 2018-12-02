package com.makecity.core.plugin.connection

import com.makecity.core.utils.ReactiveActions


/**
 * Add to [com.makecity.core.presentation.BaseViewModel] if you need observe connection state in screen
 * </br>
 * @author Andrew Chupin
 */
interface ReducerPluginConnection : ReactiveActions {

	/**
	 * Abstract variable witch will be provide connection valueOrInitial
	 */
	val connectionProvider: ConnectionProvider

	fun checkConnection() = connectionProvider.isConnected()
		.bindSubscribe(onSuccess = {
			onChangeConnection(if (it) ConnectionState.Connect else ConnectionState.Disconnect)
		})

	fun startObserveConnection() = connectionProvider.observeConnectionState()
		.bindSubscribe(onNext = {
			onChangeConnection(if (it) ConnectionState.Connect else ConnectionState.Disconnect)
		})

	fun onChangeConnection(connectionState: ConnectionState)

}
