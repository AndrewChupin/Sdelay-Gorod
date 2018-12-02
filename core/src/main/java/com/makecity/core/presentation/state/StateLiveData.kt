package com.makecity.core.presentation.state

import android.arch.lifecycle.MutableLiveData


class StateLiveData<T : ViewState> private constructor(
	private val initState: T
) : MutableLiveData<T>() {

	companion object {
		fun <VS : ViewState> create(initState: VS): StateLiveData<VS> {
			return StateLiveData(initState)
		}
	}

	init {
		value = initState
	}

	val valueOrInitial: T
		get() {
			if (value == null) {
				value = initState
			}
			return value ?: initState
		}

	fun updateValue(closure: T.() -> T) {
		value = valueOrInitial.closure()
	}
}
