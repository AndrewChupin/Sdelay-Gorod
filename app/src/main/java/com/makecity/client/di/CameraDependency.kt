package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.makecity.client.presentation.camera.CameraFragment
import com.makecity.client.presentation.camera.CameraReducer
import com.makecity.client.presentation.camera.CameraViewModel
import com.makecity.client.presentation.category.CategoryData
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
@Subcomponent(modules = [CameraModule::class])
interface CameraComponent {

	fun inject(fragment: CameraFragment)

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withFragment(fragment: Fragment): Builder
		fun build(): CameraComponent
	}

}


@Module
open class CameraModule {

	@Provides
	@FragmentScope
	fun providePermissionManager(fragment: Fragment): PermissionManager = AndroidPermissionManager(RxPermissions(fragment))

	@Provides
	@FragmentScope
	fun provideViewModelFactory(
		router: Router,
		connectionProvider: ConnectionProvider,
		permissionManager: PermissionManager
	): CameraViewModel = CameraViewModel(router, connectionProvider, permissionManager)

	@Provides
	@FragmentScope
	fun provideContract(
		fragment: Fragment,
		factory: ViewModelFactory<CameraViewModel>
	): CameraReducer = ViewModelProviders.of(fragment, factory).get(CameraViewModel::class.java)
}