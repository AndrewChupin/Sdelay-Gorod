package com.makecity.client.presentation.filter

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
data class ProblemFilterViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
class ProblemFilterAction: ActionView


// Reducer
interface ProblemFilterReducer: StatementReducer<ProblemFilterViewState, ProblemFilterAction>


// ViewModel
class ProblemFilterViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), ProblemFilterReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<ProblemFilterViewState> = StateLiveData.create(ProblemFilterViewState())

	override fun reduce(action: ProblemFilterAction) {

	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}