package com.makecity.core.presentation.state


sealed class PrimaryViewState {
    object Loading: PrimaryViewState()
    object Data: PrimaryViewState()
    data class Error(val error: String): PrimaryViewState()
}
