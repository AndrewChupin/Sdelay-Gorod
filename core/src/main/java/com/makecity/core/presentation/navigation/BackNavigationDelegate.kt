package com.makecity.core.presentation.navigation


/**
 * If you need custom back pressed button consumer add this method to your [BaseFragment]
 * </br>
 * @author Andrew Chupin
 */
interface BackNavigationDelegate {

	fun onBackClick(): Boolean

}
