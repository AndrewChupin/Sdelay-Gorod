package com.makecity.core.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject


class ViewModelFactory<VM : ViewModel> @Inject constructor(
	private val viewModel: VM
): ViewModelProvider.Factory {

	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(viewModel.javaClass)) {
			@Suppress("UNCHECKED_CAST")
			return viewModel as T
		}
		throw IllegalArgumentException("Unknown ViewModel class ${viewModel.javaClass.canonicalName} need ${modelClass.canonicalName}")
	}
}
