package com.makecity.client.presentation.profile

import com.makecity.client.app.AppScreens
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
sealed class ProfileAction: ActionView {
	object ShowEditProfile: ProfileAction()
}


// Reducer
interface ProfileReducer: StatementReducer<ProfileViewState, ProfileAction>


// ViewModel
class ProfileViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), ProfileReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<ProfileViewState> = StateLiveData.create(ProfileViewState())

	override fun reduce(action: ProfileAction) = when (action) {
		is ProfileAction.ShowEditProfile -> router.navigateTo(AppScreens.EDIT_PROFILE_SCREEN_KEY)
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}