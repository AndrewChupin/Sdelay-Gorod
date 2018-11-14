package com.makecity.core.presentation.state


sealed class PrimaryViewState {
    object Loading: PrimaryViewState()
    object Data: PrimaryViewState()
    object Success: PrimaryViewState()
    data class Error(val throwable: Throwable): PrimaryViewState()
}
