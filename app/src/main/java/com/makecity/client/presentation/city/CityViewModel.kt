package com.makecity.client.presentation.city

import com.makecity.core.data.Presentation
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ReducerPluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.BaseViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class CityViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
class CityAction: ActionView


// Reducer
interface CityReducer: StatementReducer<CityViewState, CityAction>


// ViewModel
class CityViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), CityReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<CityViewState> = StateLiveData.create(CityViewState())

	override fun reduce(action: CityAction) {

	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}