package com.makecity.client.presentation.edit_problem

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
data class EditProblemViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
class EditProblemAction: ActionView


// Reducer
interface EditProblemReducer: StatementReducer<EditProblemViewState, EditProblemAction>


// ViewModel
class EditProblemViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), EditProblemReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<EditProblemViewState> = StateLiveData.create(EditProblemViewState())

	override fun reduce(action: EditProblemAction) {

	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}