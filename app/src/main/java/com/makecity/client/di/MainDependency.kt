package com.makecity.client.di

import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
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
@Subcomponent(modules = [MainModule::class, TaskMapperModule::class])
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
interface TaskMapperModule {

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
