package com.makecity.client.presentation.views

import android.support.design.widget.CoordinatorLayout
import android.view.View

class CutomCollapseLayoutBehaviour : CoordinatorLayout.Behavior<View>() {

	override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
		return super.layoutDependsOn(parent, child, dependency)
	}

	override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
		return super.onDependentViewChanged(parent, child, dependency)
	}

}