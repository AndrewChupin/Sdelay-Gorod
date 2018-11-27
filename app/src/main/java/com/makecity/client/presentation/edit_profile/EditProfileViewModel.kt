package com.makecity.client.presentation.edit_profile

import android.Manifest
import com.makecity.client.app.AppScreens
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
import com.makecity.core.utils.permission.PermissionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// State
@Presentation
data class EditProfileViewState(
	override val screenState: PrimaryViewState = PrimaryViewState.Loading,
	val profile: Profile? = null
) : ViewState


// Action
sealed class EditProfileAction: ActionView {
	object PickPhoto: EditProfileAction()
	data class SaveChanges(
		val sex: String,
		val name: String,
		val address: String
	) : EditProfileAction()

	data class ChangePhoto(
		val photo: String
	) : EditProfileAction()

	object LoadProfile : EditProfileAction()
}

// Reducer
interface EditProfileReducer: StatementReducer<EditProfileViewState, EditProfileAction>

// ViewModel
class EditProfileViewModel(
	private val router: Router,
	private val profileDataSource: ProfileDataSource,
	override val connectionProvider: ConnectionProvider,
	private val permissionManager: PermissionManager,
	override val disposables: CompositeDisposable = CompositeDisposable()
) : BaseViewModel(), EditProfileReducer, ReducerPluginConnection {

	override val viewState: StateLiveData<EditProfileViewState> = StateLiveData.create(EditProfileViewState())

	override fun reduce(action: EditProfileAction) {
		when (action) {
			is EditProfileAction.PickPhoto -> {
				permissionManager.requestPermission(
					Manifest.permission.READ_EXTERNAL_STORAGE)
					.bindSubscribe(
						scheduler = AndroidSchedulers.mainThread(),
						onNext = {
							when {
								it.granted -> {
									router.navigateTo(AppScreens.IMAGE_PICKER_SCREEN_KEY)
								}
								it.shouldShowRequestPermissionRationale -> {

								}
								else -> {

								}
							}
						},
						onError = {
							it.printStackTrace()
						})
			}
			is EditProfileAction.ChangePhoto -> viewState.updateValue {
				copy(profile = profile?.copy(photo = action.photo))
			}
			is EditProfileAction.SaveChanges -> {
				viewState.updateValue { copy(screenState = PrimaryViewState.Loading) }
				state.profile
					?.copy(sex = action.sex, street = action.address, firstName = action.name)
					?.let { profile ->
						profileDataSource
							.deleteProfile()
							.andThen { profileDataSource.editProfile(profile) }
							.bindSubscribe(onSuccess = {
								viewState.updateValue { copy(screenState = PrimaryViewState.Success) }
							})
					}
			}
			is EditProfileAction.LoadProfile -> profileDataSource.getProfile()
				.bindSubscribe(onSuccess = {
					viewState.updateValue {
						copy(profile = it, screenState = PrimaryViewState.Data)
					}
				})
		}
	}

	// IMPLEMENT - ConnectionPlugin
	override fun onChangeConnection(connectionState: ConnectionState) {

	}
}