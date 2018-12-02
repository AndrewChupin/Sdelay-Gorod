package com.makecity.client.presentation.web

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
data class WebViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val webData: WebData
) : ViewState


// Action
sealed class WebAction : ActionView {
	object Error : WebAction()
	object Success : WebAction()
	object Exit : WebAction()
	object Loading : WebAction()
}


// Reducer
interface WebReducer : StatementReducer<WebViewState, WebAction>


// ViewModel
class WebViewModel(
	private val router: Router,
	webData: WebData,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), WebReducer, ReducerPluginConnection {

	private var isFailed: Boolean = false

	override val viewState = StateLiveData.create(WebViewState(webData = webData))

	override fun reduce(action: WebAction) = when (action) {
		is WebAction.Error -> viewState.updateValue {
			isFailed = true
			copy(screenState = PrimaryViewState.Error(IllegalArgumentException()))
		}
		is WebAction.Success -> if (!isFailed) viewState.updateValue {
			copy(screenState = PrimaryViewState.Data)
		} else Unit
		is WebAction.Loading -> viewState.updateValue {
			isFailed = false
			copy(screenState = PrimaryViewState.Loading)
		}
		is WebAction.Exit -> router.exit()
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}