package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.makecity.client.data.auth.*
import com.makecity.client.data.category.*
import com.makecity.client.data.comments.*
import com.makecity.client.data.common.Api
import com.makecity.client.data.task.*
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.main.MainNavigator
import com.makecity.client.presentation.main.MainReducer
import com.makecity.client.presentation.main.MainViewModel
import com.makecity.core.di.scope.ActivityScope
import com.makecity.core.domain.Mapper
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import javax.inject.Singleton


@ActivityScope
@Subcomponent(modules = [MainModule::class, MapperModule::class, AuthDefaultModule::class])
interface MainComponent{

	// Injects
	fun inject(mainActivity: MainActivity)

	// Subcomponents
	fun mapPointsComponent(): MapPointsComponent.Builder
	fun menuComponent(): MenuComponent.Builder
	fun notificationComponent(): NotificationComponent.Builder
	fun feedComponent(): FeedComponent.Builder
	fun webComponent(): WebComponent.Builder
	fun cityComponent(): CityComponent.Builder
	fun splashComponent(): SplashComponent.Builder
	fun aboutComponent(): AboutComponent.Builder
	fun phoneComponent(): AuthComponent.Builder
	fun problemComponent(): ProblemComponent.Builder
	fun profileComponent(): ProfileComponent.Builder
	fun editProfileComponent(): EditProfileComponent.Builder
	fun addressComponent(): AddressComponent.Builder
	fun createProblemComponent(): CreateProblemComponent.Builder
	fun editProblemComponent(): EditProblemComponent.Builder
	fun mapAddressComponent(): MapAddressComponent.Builder
	fun ownProblemsComponent(): OwnProblemsComponent.Builder
	fun problemFilterComponent(): ProblemFilterComponent.Builder
	fun settingsComponent(): SettingsComponent.Builder
	fun cameraComponent(): CameraComponent.Builder
	fun categoryComponent(): CategoryComponent.Builder
	fun descriptionComponent(): DescriptionComponent.Builder

	@Subcomponent.Builder
	interface Builder {
		@BindsInstance
		fun withActivity(fragmentActivity: FragmentActivity): Builder

		@BindsInstance
		fun withFragmentManager(fragmentManager: FragmentManager): Builder

		@BindsInstance
		fun withContainer(@IdRes containerId: Int): Builder

		fun build(): MainComponent
	}

}



@Module
interface MapperModule {

	@Singleton
	@Binds
	fun provideTaskMapperDtoToPersistence(mapper: ProblemMapperDtoToPersistence): Mapper<TaskRemote, TaskPersistence>

	@Singleton
	@Binds
	fun provideTaskMapperPersistenceToCommon(mapper: ProblemMapperPersistenceToCommon): Mapper<TaskPersistence, Task>


	@Singleton
	@Binds
	fun provideCommentsAuthorMapperDtoToPersistence(mapper: CommentsAuthorMapperDtoToPersistence): Mapper<CommentRemote, CommentPersistence>

	@Singleton
	@Binds
	fun provideCommentsAuthorMapperPersistenceToCommon(mapper: CommentsAuthorMapperPersistenceToCommon): Mapper<CommentPersistence, Comment>


	@Singleton
	@Binds
	fun provideCategoryMapperDtoToPersistence(mapper: CategoryMapperDtoToPersist): Mapper<CategoryRemote, CategoryPersistence>

	@Singleton
	@Binds
	fun provideCategoryMapperPersistenceToCommon(mapper: CategoryMapperPersistToCommon): Mapper<CategoryPersistence, Category>

	@Singleton
	@Binds
	fun provideAuthNextStepMapper(mapper: AuthNextStepMapper): Mapper<String, NextAuthStep>
}


@Module
class AuthDefaultModule {

	@Provides
	@ActivityScope
	fun provideAuthService(authServiceDefault: AuthServiceDefault): AuthService = authServiceDefault

	@Provides
	@ActivityScope
	fun provideAuthStorage(authStorage: AuthStoragePreferences): AuthStorage = authStorage

	@Provides
	@ActivityScope
	fun provideAuthDataSource(
		authService: AuthService,
		authStorage: AuthStorage,
		mapper: AuthNextStepMapper
	): AuthDataSource = AuthDataSourceDefault(authService, authStorage, mapper)
}


@Module
class MainModule {

	@Provides
	@ActivityScope
	fun provideService(api: Api): ProblemService = ProblemServiceRetrofit(api)

	@Provides
	@ActivityScope
	fun provideTaskService(
		problemService: ProblemService,
		mapperPersistence: ProblemMapperDtoToPersistence,
		mapperCommon: ProblemMapperPersistenceToCommon,
		mapperCommentsDto: CommentsAuthorMapperDtoToPersistence,
		mapperCommentsPersist: CommentsAuthorMapperPersistenceToCommon
	): ProblemDataSource = ProblemDataSourceRemote(problemService, mapperPersistence, mapperCommon, mapperCommentsDto, mapperCommentsPersist)

	@Provides
	@ActivityScope
	fun provideNavigator(mainNavigator: MainNavigator): Navigator = mainNavigator

	@Provides
	@ActivityScope
	fun provideViewModel(router: Router): MainViewModel = MainViewModel(router)

	@Provides
	@ActivityScope
	fun provideContract(
		fragmentActivity: FragmentActivity,
		mainViewModelFactory: ViewModelFactory<MainViewModel>
	): MainReducer = ViewModelProviders.of(fragmentActivity, mainViewModelFactory).get(MainViewModel::class.java)
}
