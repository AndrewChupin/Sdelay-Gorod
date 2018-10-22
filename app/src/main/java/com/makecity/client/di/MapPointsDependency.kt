package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import com.makecity.client.data.task.ProblemDataSource
import com.makecity.client.domain.map.TaskInteractorReactive
import com.makecity.client.domain.map.TaskPointsInteractor
import com.makecity.client.presentation.map.MapPointsFragment
import com.makecity.client.presentation.map.MapPointsReducer
import com.makecity.client.presentation.map.MapPointsViewModel
import com.makecity.core.di.scope.FragmentScope
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
@Subcomponent(modules = [MapPointsModule::class])
interface MapPointsComponent {

	fun inject(mapPointsFragment: MapPointsFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): MapPointsComponent
	}

}


@Module
open class MapPointsModule {

	@Provides
	@FragmentScope
	fun provideMapPointsInteractor(problemDataSource: ProblemDataSource): TaskPointsInteractor = TaskInteractorReactive(problemDataSource)

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
		interactor: TaskPointsInteractor,
		connectionProvider: ConnectionProvider,
		permissionManager: PermissionManager,
		locationProvider: LocationProvider
	): MapPointsViewModel = MapPointsViewModel(router, interactor, connectionProvider, permissionManager, locationProvider)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<MapPointsViewModel>
	): MapPointsReducer = ViewModelProviders.of(fragment, factory).get(MapPointsViewModel::class.java)
}
