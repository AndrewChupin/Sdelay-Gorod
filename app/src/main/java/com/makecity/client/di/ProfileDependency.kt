package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.profile.ProfileFragment
import com.makecity.client.presentation.profile.ProfileReducer
import com.makecity.client.presentation.profile.ProfileViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import com.makecity.core.utils.resources.ResourceManager
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

	fun inject(fragment: ProfileFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): ProfileComponent
	}

}


@Module
open class ProfileModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): ProfileViewModel = ProfileViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<ProfileViewModel>
	): ProfileReducer = ViewModelProviders.of(fragment, factory).get(ProfileViewModel::class.java)
}