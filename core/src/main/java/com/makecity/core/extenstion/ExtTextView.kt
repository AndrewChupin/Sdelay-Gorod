package com.makecity.core.extenstion

import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import java.util.regex.Pattern


data class ExecutableRange(
	val from: Int,
	val to: Int
)


inline fun TextView.textWithExecutableHref(htmlText: String, crossinline onExecute: (String) -> Unit) {
	val hrefRegex = "<a href=\"([^>]+)\">((?:.(?!</a>))*.)</a>"
	var result = htmlText

	val groups = mutableMapOf<String, ExecutableRange>()

	val pattern = Pattern.compile(hrefRegex)
	val matcher = pattern.matcher(result)

	while (matcher.find()) {
		var url = ""
		for (i in 1..matcher.groupCount()) {
			val match = matcher.group(i)
			if (i % 2 == 0) {
				val indexFrom = result.indexOfFirst { it == '<' && result[result.indexOf(it).inc()] == 'a' }
				result = result.replaceFirst("<a href=\"[^>]+\">".toRegex(), "")
					.replaceFirst("</a>", "")
				groups[url] = ExecutableRange(indexFrom , indexFrom + match.length)
			} else {
				url = match
			}
		}
	}
	val spannableBuilder = SpannableStringBuilder(result)
	groups.forEach { entry ->
		spannableBuilder.setSpan(object : ClickableSpan() {
			override fun onClick(widget: View) {
				onExecute(entry.key)
			}
		}, entry.value.from, entry.value.to, 0)
	}
	text = spannableBuilder
}
