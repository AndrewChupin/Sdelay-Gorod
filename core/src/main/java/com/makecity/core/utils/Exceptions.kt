package com.makecity.core.utils


class NoLastKnownLocation(error: String = "Location from FusedLocationProvider is null")
	: RuntimeException(error)


class NoSuchDataException(
	override val message: String?
): RuntimeException(message)


object WritePersistenceException: RuntimeException()