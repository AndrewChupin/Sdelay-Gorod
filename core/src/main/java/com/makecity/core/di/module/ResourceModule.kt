package com.makecity.core.di.module

import com.makecity.core.utils.resources.AndroidResourceManager
import com.makecity.core.utils.resources.ResourceManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface ResourceModule {

	@Singleton
	@Binds
	fun provideResourceManager(manager: AndroidResourceManager): ResourceManager

}
