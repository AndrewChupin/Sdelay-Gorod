package com.makecity.core.utils.permission

import com.tbruyelle.rxpermissions2.Permission
import io.reactivex.Observable


/**
 * Interface wrapper for permission request
 * @author Andrew Chupin
 */
interface PermissionManager {

	// TODO WRAP [Permission] CLASS
	/**
	 * Execute request by array
	 * @param permissionCode contains array with permission codes
	 */
	fun requestPermission(vararg permissionCode: String): Observable<Permission>

}
