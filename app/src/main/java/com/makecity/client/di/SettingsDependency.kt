package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.settings.SettingsFragment
import com.makecity.client.presentation.settings.SettingsReducer
import com.makecity.client.presentation.settings.SettingsViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [SettingsModule::class])
interface SettingsComponent {

	fun inject(fragment: SettingsFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): SettingsComponent
	}

}


@Module
open class SettingsModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): SettingsViewModel = SettingsViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<SettingsViewModel>
	): SettingsReducer = ViewModelProviders.of(fragment, factory).get(SettingsViewModel::class.java)
}