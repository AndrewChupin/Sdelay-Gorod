package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.text.style.BulletSpan
import com.makecity.client.data.address.*
import com.makecity.client.data.temp_problem.TempProblemDataSource
import com.makecity.client.presentation.map_address.MapAddressFragment
import com.makecity.client.presentation.map_address.MapAddressReducer
import com.makecity.client.presentation.map_address.MapAddressScreenData
import com.makecity.client.presentation.map_address.MapAddressViewModel
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.domain.Mapper
import com.makecity.core.plugin.connection.ConnectionProvider
import com.makecity.core.plugin.location.GoogleLocationProvider
import com.makecity.core.plugin.location.LocationProvider
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
@Subcomponent(modules = [MapAddressModule::class])
interface MapAddressComponent {

	fun inject(fragment: MapAddressFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		@BindsInstance
		fun withData(data: MapAddressScreenData): Builder
		fun build(): MapAddressComponent
	}
}


@Module
open class MapAddressModule {

	@Provides
	@FragmentScope
	fun provideAddressService(service: AddressServiceRetrofit): AddressService = service

	@Provides
	@FragmentScope
	fun provideAddressMapperRemoteToCommon(mapper: AddressMapperRemoteToCommon): Mapper<AddressRemote, Address> = mapper

	@Provides
	@FragmentScope
	fun provideAddressDataSource(
		service: AddressService,
		mapper: AddressMapperRemoteToCommon
	): AddressDataSource = AddressDataSourceDefault(service, mapper)

	@Provides
	@FragmentScope
	fun providePermissionManager(fragment: Fragment): PermissionManager = AndroidPermissionManager(RxPermissions(fragment))

	@Provides
	@FragmentScope
	fun provideLocationProvider(context: Context): LocationProvider = GoogleLocationProvider(context)

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		data: MapAddressScreenData,
		dataSource: AddressDataSource,
		tempProblemDataSource: TempProblemDataSource,
		connectionProvider: ConnectionProvider,
		permissionManager: PermissionManager,
		locationProvider: LocationProvider
	): MapAddressViewModel = MapAddressViewModel(router, data, tempProblemDataSource, dataSource, connectionProvider, permissionManager, locationProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<MapAddressViewModel>
	): MapAddressReducer = ViewModelProviders.of(fragment, factory).get(MapAddressViewModel::class.java)
}