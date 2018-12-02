package com.makecity.client.data.category

import com.makecity.core.domain.Mapper
import com.makecity.core.utils.Symbols.EMPTY
import javax.inject.Inject


class CategoryMapperDtoToPersist @Inject constructor() : Mapper<CategoryRemote, CategoryPersistence> {

	override fun transform(entity: CategoryRemote): CategoryPersistence = entity.run {
		CategoryPersistence(
			id = id,
			name = name ?: EMPTY,
			options = options?.map {
				OptionPersistence(
					id = it.id,
					name = it.name ?: EMPTY
				)
			} ?: emptyList()
		)
	}
}

class CategoryMapperPersistToCommon @Inject constructor() : Mapper<CategoryPersistence, Category> {

	override fun transform(entity: CategoryPersistence): Category = entity.run {
		Category(
			id = id,
			name = name,
			options = options.map {
				Option(
					id = it.id,
					name = it.name
				)
			}
		)
	}
}