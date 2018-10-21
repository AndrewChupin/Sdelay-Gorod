package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.map_address.MapAddressFragment
import com.makecity.client.presentation.map_address.MapAddressReducer
import com.makecity.client.presentation.map_address.MapAddressViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(modules = [MapAddressModule::class])
interface MapAddressComponent {

	fun inject(fragment: MapAddressFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): MapAddressComponent
	}

}


@Module
open class MapAddressModule {

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider
	): MapAddressViewModel = MapAddressViewModel(router, connectionProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<MapAddressViewModel>
	): MapAddressReducer = ViewModelProviders.of(fragment, factory).get(MapAddressViewModel::class.java)
}