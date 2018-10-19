package com.makecity.client.presentation.profile

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
data class ProfileViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
class ProfileAction: ActionView


// Reducer
interface ProfileReducer: StatementReducer<ProfileViewState, ProfileAction>


// ViewModel
class ProfileViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), ProfileReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<ProfileViewState> = StateLiveData.create(ProfileViewState())

	override fun reduce(action: ProfileAction) {

	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}