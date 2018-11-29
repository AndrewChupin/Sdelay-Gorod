package com.makecity.client.presentation.views

import android.content.Context
import android.widget.LinearLayout
import android.util.AttributeSet
import com.makecity.client.R


class TitledEditText : LinearLayout {

	init {
		inflate(context, R.layout.view_titled_edit_text, this)
		orientation = VERTICAL
	}

	constructor(ctx: Context) : super(ctx)

	constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

}