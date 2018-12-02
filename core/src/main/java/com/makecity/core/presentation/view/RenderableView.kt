package com.makecity.core.presentation.view

import com.makecity.core.presentation.state.ViewState


/**
 * This interface mean that screen support single valueOrInitial
 * You can use it with [com.makecity.core.presentation.BaseViewModel]
 * [ViewState] and [com.makecity.core.presentation.BaseFragment]
 */
interface RenderableView<in State : ViewState> {

	/**
	 * This function must be refresh all screen valueOrInitial
	 */
	fun render(state: State)
}
