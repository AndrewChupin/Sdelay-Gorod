package com.makecity.core.presentation.navigation

import android.content.Intent


/**
 * If you need custom back pressed button consumer add this method to your [BaseFragment]
 * </br>
 * @author Andrew Chupin
 */
interface FragmentDelegate {

	fun onBackClick(): Boolean
	fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?)

}
