package com.makecity.client.presentation.problem

import android.content.Context
import android.util.AttributeSet
import com.makecity.core.presentation.view.map.BaseMapView


class ProblemsMapView: BaseMapView {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		getMapAsync(this)
	}
}