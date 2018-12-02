package com.makecity.client.presentation.profile

import com.makecity.client.app.AppScreens
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.client.data.geo.GeoPoint
import com.makecity.client.data.profile.Profile
import com.makecity.client.data.profile.ProfileDataSource
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
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import ru.terrakok.cicerone.Router


// State
@Presentation
data class ProfileViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val profile: Profile? = null,
	val geoPoint: GeoPoint? = null
) : ViewState


// Action
sealed class ProfileAction : ActionView {
	object ShowEditProfile : ProfileAction()
	object GetProfileData : ProfileAction()
	object Logout : ProfileAction()
}


// Reducer
interface ProfileReducer : StatementReducer<ProfileViewState, ProfileAction>


// ViewModel
class ProfileViewModel(
	private val router: Router,
	private val profileDataSource: ProfileDataSource,
	private val geoDataSource: GeoDataSource,
	override val connectionProvider: ConnectionProvider,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), ProfileReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<ProfileViewState> = StateLiveData.create(ProfileViewState())

	override fun reduce(action: ProfileAction) {
		when (action) {
			is ProfileAction.ShowEditProfile -> router.navigateTo(AppScreens.EDIT_PROFILE_SCREEN_KEY)
			is ProfileAction.GetProfileData -> onGetProfile()
			is ProfileAction.Logout -> onLogout()
		}
	}

	private fun onGetProfile() = Maybe.zip(
		profileDataSource.getProfile(),
		geoDataSource.getDefaultGeoPoint(),
		BiFunction<Profile, GeoPoint, Pair<Profile, GeoPoint>> { profile, geo -> Pair(profile, geo) }
	).bindSubscribe(onSuccess = {
		viewState.updateValue { copy(profile = it.first, geoPoint = it.second) }
	}, onComplete = {

	})

	private fun onLogout() = profileDataSource
		.deleteProfile()
		.bindSubscribe(onSuccess = {
			router.exit()
		})


	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}