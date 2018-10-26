package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.geo.*
import com.makecity.client.presentation.splash.SplashFragment
import com.makecity.client.presentation.splash.SplashReducer
import com.makecity.client.presentation.splash.SplashViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.domain.Mapper
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import com.makecity.core.utils.resources.ResourceManager
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

	fun inject(fragment: SplashFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): SplashComponent
	}
}


@Module
open class SplashModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		geoDataSource: GeoDataSource,
		resourceManager: ResourceManager,
		connectionProvider: ConnectionProvider
	): SplashViewModel = SplashViewModel(router, geoDataSource, resourceManager, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<SplashViewModel>
	): SplashReducer = ViewModelProviders.of(fragment, factory).get(SplashViewModel::class.java)
}