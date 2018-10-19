package com.makecity.core.presentation.screen

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar


data class ToolbarConfig(
	val title: String? = null,
	val isDisplayHomeButton: Boolean = true,
	val isEnableHomeButton: Boolean = true,
	val drawable: Drawable? = null
)


interface ToolbarScreen {

	fun getToolbar(): Toolbar

	fun setupToolbarWith(activity: Activity, toolbarConfig: ToolbarConfig = ToolbarConfig()) {
		if (activity is AppCompatActivity) {
			activity.setSupportActionBar(getToolbar())
			activity.supportActionBar?.apply {
				toolbarConfig.drawable?.let {
					setBackgroundDrawable(it)
				}

				setDisplayHomeAsUpEnabled(toolbarConfig.isDisplayHomeButton)
				setHomeButtonEnabled(toolbarConfig.isEnableHomeButton)
			}
		}

		toolbarConfig.title?.let {
			getToolbar().title = it
		}
	}
}
