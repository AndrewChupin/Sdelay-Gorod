package com.makecity.client.data.category

import com.makecity.client.data.common.Api
import io.reactivex.Single
import javax.inject.Inject


interface CategoryService {
	fun loadCategories(): Single<List<CategoryRemote>>
}


class CategoryServiceDefault @Inject constructor(
	private val api: Api
): CategoryService {

	override fun loadCategories(): Single<List<CategoryRemote>> = api.loadCategories()

}