package com.makecity.client.data.category

import com.makecity.core.domain.Mapper
import io.reactivex.Single
import javax.inject.Inject


interface CategoryDataSource {
	fun getCategories(): Single<List<Category>>
	fun getCategory(categoryId: Long): Single<Category>
}


class CategoryDataSourceDefault @Inject constructor(
	private val categoryService: CategoryService,
	private val categoryMapperDtoToPersist: Mapper<CategoryRemote, CategoryPersistence>,
	private val categoryMapperPersistToCommon: Mapper<CategoryPersistence, Category>
) : CategoryDataSource {

	private var categories: List<Category> = emptyList()

	override fun getCategories(): Single<List<Category>> = Single.defer {
		if (categories.isEmpty()) {
			categoryService.loadCategories()
				.map(categoryMapperDtoToPersist::transformAll)
				.map(categoryMapperPersistToCommon::transformAll)
				.doOnSuccess { categories = it }
		} else {
			Single.just(categories)
		}
	}

	override fun getCategory(categoryId: Long): Single<Category> = Single.defer {
		getCategories().map { list ->
			list.find { it.id == categoryId }
		}
	}
}