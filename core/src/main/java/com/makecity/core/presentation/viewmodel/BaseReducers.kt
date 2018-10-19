package com.makecity.core.presentation.viewmodel

import com.makecity.core.presentation.state.ViewState


interface BaseReducer<AG: ActionView> {
    fun reduce(action: AG)
}


interface StatementReducer<State: ViewState, AG: ActionView>: BaseReducer<AG>, ReducerPluginViewState<State> {

    val state: State
        get() = viewState.valueOrInitial

    fun reduceBeforeReady() {}
    fun reduceAfterReady() {}
}
