package com.makecity.core.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import com.makecity.core.utils.ReactiveActions


abstract class BaseViewModel: ViewModel()


abstract class ReactiveViewModel: BaseViewModel(), ReactiveActions {

	@CallSuper
	override fun onCleared() {
		super.onCleared()
		clearDisposables()
	}
}
