package com.makecity.client.presentation.views

import android.content.Context
import android.widget.LinearLayout
import android.util.AttributeSet
import com.makecity.client.R
import com.makecity.core.utils.Symbols.EMPTY
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.view_titled_edit_text.view.*


class TitledEditText : LinearLayout {

	lateinit var title: String
	lateinit var hint: String
	lateinit var text: String

	init {
		inflate(context, R.layout.view_titled_edit_text, this)
		orientation = VERTICAL
	}

	constructor(ctx: Context) : super(ctx)

	constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
		init(attrs)
	}

	private fun init(attrs: AttributeSet) {
		val array = context.obtainStyledAttributes(attrs,
			R.styleable.TitledEditText)

		title = array.getString(R.styleable.TitledEditText_title) ?: EMPTY
		hint = array.getString(R.styleable.TitledEditText_android_hint) ?: EMPTY
		text = array.getString(R.styleable.TitledEditText_android_text) ?: EMPTY

		array.recycle()
	}

	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		titled_view_title.text = title
		titled_view_field.setText(text)
		titled_view_field.hint = hint
	}


	override fun onDetachedFromWindow() {
		super.onDetachedFromWindow()
		clearFindViewByIdCache()
	}
}