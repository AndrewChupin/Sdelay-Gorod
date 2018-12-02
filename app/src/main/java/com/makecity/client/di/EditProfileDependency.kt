package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.profile.ProfileDataSource
import com.makecity.client.presentation.edit_profile.EditProfileFragment
import com.makecity.client.presentation.edit_profile.EditProfileReducer
import com.makecity.client.presentation.edit_profile.EditProfileViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import com.makecity.core.utils.permission.AndroidPermissionManager
import com.makecity.core.utils.permission.PermissionManager
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [EditProfileModule::class])
interface EditProfileComponent {

	fun inject(fragment: EditProfileFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): EditProfileComponent
	}

}


@Module
open class EditProfileModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider,
		profileDataSource: ProfileDataSource,
		permissionManager: PermissionManager
	): EditProfileViewModel = EditProfileViewModel(router, profileDataSource, connectionProvider, permissionManager)

	@Provides
	@FragmentScope
	fun providePermissionManager(fragment: Fragment): PermissionManager = AndroidPermissionManager(RxPermissions(fragment))

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<EditProfileViewModel>
	): EditProfileReducer = ViewModelProviders.of(fragment, factory).get(EditProfileViewModel::class.java)
}