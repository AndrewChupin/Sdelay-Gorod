package com.makecity.client.presentation.description

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
data class DescriptionViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading
) : ViewState


// Action
sealed class DescriptionAction: ActionView {
	object ShowMapAddress: DescriptionAction()
}


// Reducer
interface DescriptionReducer: StatementReducer<DescriptionViewState, DescriptionAction>


// ViewModel
class DescriptionViewModel(
	private val router: Router,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), DescriptionReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<DescriptionViewState> = StateLiveData.create(DescriptionViewState())

	override fun reduce(action: DescriptionAction) {
		if (action is DescriptionAction.ShowMapAddress) {
			router.navigateTo(AppScreens.MAP_ADDRESS_SCREEN_KEY)
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}