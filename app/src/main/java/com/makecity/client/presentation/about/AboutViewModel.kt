package com.makecity.client.presentation.about

import com.makecity.client.BuildConfig
import com.makecity.client.app.AppScreens
import com.makecity.client.presentation.web.WebData
import com.makecity.core.plugin.connection.ConnectionState
import com.makecity.core.plugin.connection.ViewStatePluginConnection
import com.makecity.core.presentation.state.PrimaryViewState
import com.makecity.core.presentation.state.StateLiveData
import com.makecity.core.presentation.state.ViewState
import com.makecity.core.presentation.viewmodel.ActionView
import com.makecity.core.presentation.viewmodel.ReactiveViewModel
import com.makecity.core.presentation.viewmodel.StatementReducer
import com.makecity.core.utils.Symbols
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

// Actions
sealed class AboutAction : ActionView

object ShowSupportScreen : AboutAction()


// State
data class AboutViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	override val connectionState: ConnectionState = ConnectionState.Unknown
) : ViewState, ViewStatePluginConnection


// Reducer
interface AboutReducer : StatementReducer<AboutViewState, AboutAction>


// View Model
class AboutViewModel @Inject constructor(
	private val router: Router,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : ReactiveViewModel(), AboutReducer {

	override val viewState = StateLiveData.create(AboutViewState())

	override fun reduce(action: AboutAction) = when (action) {
		is ShowSupportScreen -> router.navigateTo(AppScreens.WEB_SCREEN_KEY,
			WebData(BuildConfig.SUPPORT_URL, Symbols.EMPTY))
	}
}