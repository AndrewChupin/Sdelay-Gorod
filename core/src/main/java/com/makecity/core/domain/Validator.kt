package com.makecity.core.domain


interface Validator<Request, Response> {
	fun validate(vararg requests: Request): Response
}