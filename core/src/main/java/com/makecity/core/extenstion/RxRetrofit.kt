package com.makecity.core.extenstion

import okhttp3.MultipartBody
import okhttp3.RequestBody

fun String.part(): RequestBody = RequestBody.create(MultipartBody.FORM, this)

