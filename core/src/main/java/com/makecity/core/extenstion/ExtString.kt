package com.makecity.core.extenstion

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.makecity.core.utils.Symbols.SLASH
import java.lang.StringBuilder


@Suppress("NOTHING_TO_INLINE")
inline fun String.joinPath(vararg subPaths: String?): String {
	val builder = StringBuilder()
	builder.append(this)
	if (!this.endsWith(SLASH)) {
		builder.append(SLASH)
	}
	subPaths.forEachIndexed { index, path ->
		path?.let {
			builder.append(it)
			if (index != subPaths.size - 1) {
				builder.append(SLASH)
			}
		}
	}
	return builder.toString()
}


inline fun String.checkNotEmpty(closure: (String) -> Unit) {
	if (isNotEmpty()) closure(this)
}


@Suppress("NOTHING_TO_INLINE", "DEPRECATION")
val String.fromHtml: Spanned
	get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
		Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
	} else {
		Html.fromHtml(this)
	}
