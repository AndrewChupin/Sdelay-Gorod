package com.makecity.core.utils


class NoLastKnownLocation(error: String = "Location from FusedLocationProvider is null")
	: RuntimeException(error)


object DataStructureException : RuntimeException()

class NoSuchDataException(
	override val message: String?
) : RuntimeException(message)


object WritePersistenceException : RuntimeException()