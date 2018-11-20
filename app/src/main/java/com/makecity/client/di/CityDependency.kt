package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.data.geo.GeoDataSource
import com.makecity.client.presentation.city.CityFragment
import com.makecity.client.presentation.city.CityReducer
import com.makecity.client.presentation.city.CityViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [CityModule::class])
interface CityComponent {

	fun inject(fragment: CityFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): CityComponent
	}

}


@Module
open class CityModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider,
		geoDataSource: GeoDataSource
	): CityViewModel = CityViewModel(router, geoDataSource, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CityViewModel>
	): CityReducer = ViewModelProviders.of(fragment, factory).get(CityViewModel::class.java)
}