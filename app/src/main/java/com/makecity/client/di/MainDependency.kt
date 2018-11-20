package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.makecity.client.data.auth.*
import com.makecity.client.data.category.*
import com.makecity.client.data.comments.*
import com.makecity.client.data.common.Api
import com.makecity.client.data.geo.*
import com.makecity.client.data.profile.*
import com.makecity.client.data.task.*
import com.makecity.client.data.temp_problem.*
import com.makecity.client.presentation.main.MainActivity
import com.makecity.client.presentation.main.MainNavigator
import com.makecity.client.presentation.main.MainReducer
import com.makecity.client.presentation.main.MainViewModel
import com.makecity.core.di.scope.ActivityScope
import com.makecity.core.di.scope.FragmentScope
import com.makecity.core.domain.Mapper
import com.makecity.core.presentation.viewmodel.ViewModelFactory
import dagger.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import javax.inject.Singleton


@ActivityScope
@Subcomponent(modules = [
	MainModule::class,
	MapperModule::class,
	AuthDefaultModule::class,
	TempProblemModule::class,
	GeoPointModule::class,
	CommentsDataModule::class,
	ProfileDataModule::class
])
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
	fun restoreComponent(): RestoreComponent.Builder
	fun commentsComponent(): CommentsComponent.Builder

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
class CommentsDataModule {

	@Provides
	@ActivityScope
	fun provideCommentsService(service: CommentServiceRetrofit): CommentService = service


	@Provides
	@ActivityScope
	fun provideDataSource(
		service: CommentService,
		mapperDto: CommentsAuthorMapperDtoToPersistence,
		mapperPersist: CommentsAuthorMapperPersistenceToCommon
	): CommentsDataSource = CommentsDataSourceDefault(service, mapperDto, mapperPersist)
}


@Module
class ProfileDataModule {

	@Provides
	@ActivityScope
	fun provideProfileService(service: ProfileServiceRetrofit): ProfileService = service

	@Provides
	@ActivityScope
	fun provideProfileStorage(storage: ProfileStorageRoom): ProfileStorage = storage

	@Provides
	@ActivityScope
	fun provideMapperDto(mapper: ProfileDtoToPersistMapper): Mapper<ProfileRemote, ProfilePersistence> = mapper

	@Provides
	@ActivityScope
	fun provideMapperPersist(mapper: ProfilePersistToCommonMapper): Mapper<ProfilePersistence, Profile> = mapper

	@Provides
	@ActivityScope
	fun provideDataSource(source: ProfileDataSourceDefault): ProfileDataSource = source

}


@Module
class GeoPointModule {

	@Provides
	@ActivityScope
	fun provideGeoPointRemoteToPersist(
		mapper: GeoPointRemoteToPersist
	): Mapper<GeoPointRemote, GeoPointPersistence> = mapper

	@Provides
	@ActivityScope
	fun provideGeoPointPersistToCommon(
		mapper: GeoPointPersistToCommon
	): Mapper<GeoPointPersistence, GeoPoint> = mapper

	@Provides
	@ActivityScope
	fun provideGeoService(
		geoServiceRetrofit: GeoServiceRetrofit
	): GeoService = geoServiceRetrofit

	@Provides
	@ActivityScope
	fun provideGeoPointStorage(
		geoPointStoragePreference: GeoPointStoragePreference
	): GeoPointStorage = geoPointStoragePreference

	@Provides
	@ActivityScope
	fun provideGeoPointDataSource(
		geoService: GeoService,
		geoPointStorage: GeoPointStorage,
		mapperRemoteToPersist: GeoPointRemoteToPersist,
		mapperPersistToCommon: GeoPointPersistToCommon
	): GeoDataSource = GeoDataSourceDefault(geoService, geoPointStorage, mapperRemoteToPersist, mapperPersistToCommon)

}

@Module
class TempProblemModule {

	@Provides
	@ActivityScope
	fun provideTempProblemMapperCommonToPersistence(
		mapperCommonToPersistence: TempProblemMapperCommonToPersistence
	): Mapper<TempProblem, TempProblemPersistence> = mapperCommonToPersistence

	@Provides
	@ActivityScope
	fun provideTempProblemMapperPersistenceToCommon(
		mapperPersistenceToCommon: TempProblemMapperPersistenceToCommon
	): Mapper<TempProblemPersistence, TempProblem> = mapperPersistenceToCommon

	@Provides
	@ActivityScope
	fun provideTempProblemStorage(problemStorageRoom: TempProblemStorageRoom): TempProblemStorage = problemStorageRoom

	@Provides
	@ActivityScope
	fun provideTempProblemService(tempTaskService: TempTaskServiceRetrofit): TempTaskService = tempTaskService

	@Provides
	@ActivityScope
	fun provideTempProblemDataSource(
		mapperCommonToPersistence: TempProblemMapperCommonToPersistence,
		mapperPersistenceToCommon: TempProblemMapperPersistenceToCommon,
		tempTaskService: TempTaskService,
		authDataSource: AuthDataSource,
		geoDataSource: GeoDataSource,
		storage: TempProblemStorage
	): TempProblemDataSource = TempProblemDataSourceDefault(storage, tempTaskService, geoDataSource,
		authDataSource, mapperCommonToPersistence, mapperPersistenceToCommon)
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
		geoDataSource: GeoDataSource,
		mapperCommon: ProblemMapperPersistenceToCommon,
		mapperCommentsDto: CommentsAuthorMapperDtoToPersistence,
		mapperCommentsPersist: CommentsAuthorMapperPersistenceToCommon
	): ProblemDataSource = ProblemDataSourceRemote(problemService, geoDataSource, mapperPersistence, mapperCommon, mapperCommentsDto, mapperCommentsPersist)

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
