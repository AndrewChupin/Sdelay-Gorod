package com.makecity.core.presentation.viewmodel

import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.state.StateLiveData


/**
 * This architecture using global valueOrInitial on each screen.
 * Add to [ViewModel] if controllable screen support single [ViewState]
 * </br>
 * @author Andrew Chupin
 */
interface ReducerPluginViewState<State: ViewState> {
	val viewState: StateLiveData<State>
}
