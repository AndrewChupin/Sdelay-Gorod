package com.makecity.core.extenstion

import android.support.design.widget.BottomNavigationView
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.util.Log


fun BottomNavigationView.setShiftMode(isEnable: Boolean) {
	val menuView = getChildAt(0) as BottomNavigationMenuView
	try {
		val shiftingMode = menuView.javaClass.getDeclaredField("isShifting")
		shiftingMode.isAccessible = true
		shiftingMode.setBoolean(menuView, false)
		shiftingMode.isAccessible = false
		for (i in 0 until menuView.childCount) {
			val item = menuView.getChildAt(i) as BottomNavigationItemView
			item.setShifting(false)
			// set once again checked value, so view will be updated
			item.setChecked(item.itemData.isChecked)
		}
	} catch (e: NoSuchFieldException) {
	} catch (e: IllegalAccessException) {
		Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
	}
}