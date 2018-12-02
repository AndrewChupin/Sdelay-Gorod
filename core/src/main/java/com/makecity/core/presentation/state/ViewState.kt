package com.makecity.core.presentation.state


/**
 * This architecture using global valueOrInitial on each screen.
 * </br>
 * If you creating new screen your new State for this screen must be implement this interface
 * for integration with a base classes [com.makecity.core.presentation.BaseViewModel]
 * and [com.makecity.core.presentation.BaseFragment]...
 * </br>
 * @author Andrew Chupin
 */
interface ViewState {
	val screenState: PrimaryViewState
}
