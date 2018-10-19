package com.makecity.core.presentation.view

import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseReducer


/**
 * This interface mean that object using controller as [BaseViewModel]
 * </br>
 * @author Andrew Chupin
 */
interface ReducibleView<Reducer: BaseReducer<AG>, AG: ActionView> {
	/**
	 * [BaseViewModel] which you can provide with [javax.inject.Inject] annotation
	 */
	var reducer: Reducer
}
