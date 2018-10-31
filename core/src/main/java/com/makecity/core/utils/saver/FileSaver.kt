package com.makecity.core.utils.saver

import java.io.File


interface SaverRequest {
	val file: File
}

interface FileSaver {
	fun save(request: SaverRequest)
}


