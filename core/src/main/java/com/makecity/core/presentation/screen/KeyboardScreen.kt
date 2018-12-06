package com.makecity.core.presentation.screen

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.JobIntentService
import android.view.inputmethod.InputMethodManager


interface KeyboardScreen {

	fun Fragment.hideKeyboard() {
		requireActivity().currentFocus?.let {
			val inputManager = requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.hideSoftInputFromWindow(it.windowToken, 0)
		}
	}

	fun Fragment.showKeyboard() {
		requireActivity().currentFocus.let {
			val inputManager = requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
		}
	}
}