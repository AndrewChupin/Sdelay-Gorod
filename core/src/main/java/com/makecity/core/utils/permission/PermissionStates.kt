package com.makecity.core.utils.permission


sealed class PermissionState {
	data class Granted(val name: String) : PermissionState()
	data class Rationale(val name: String) : PermissionState()
	data class Unknown(val name: String) : PermissionState()
	data class Failure(val throwable: Throwable) : PermissionState()
}
