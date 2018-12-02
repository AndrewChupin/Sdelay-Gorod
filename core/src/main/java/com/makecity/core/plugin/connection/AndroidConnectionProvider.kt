package com.makecity.core.plugin.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class AndroidConnectionProvider @Inject constructor(
	context: Context,
	private val manager: ConnectivityManager
) : ConnectionProvider, BroadcastReceiver() {

	private val subject = BehaviorSubject.createDefault<Boolean>(checkStatus())

	init {
		context.registerReceiver(this, IntentFilter(CONNECTIVITY_ACTION))
	}

	override fun isConnected(): Single<Boolean> = Single.fromCallable {
		checkStatus()
	}

	override fun observeConnectionState(): Observable<Boolean> = subject

	override fun onReceive(context: Context?, intent: Intent?) {
		publishStatus()
	}

	private fun checkStatus(): Boolean {
		val netInfo = manager.activeNetworkInfo
		return netInfo != null && netInfo.isConnectedOrConnecting
	}

	private fun publishStatus() {
		subject.onNext(checkStatus())
	}
}
