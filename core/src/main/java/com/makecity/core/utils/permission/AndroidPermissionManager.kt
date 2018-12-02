package com.makecity.core.utils.permission

import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import javax.inject.Inject


class AndroidPermissionManager @Inject constructor(
	private val rxPermissions: RxPermissions
) : PermissionManager {

	override fun requestPermission(vararg permissionCode: String): Observable<Permission> = Observable.defer {
		rxPermissions.requestEach(*permissionCode)
	}
}
