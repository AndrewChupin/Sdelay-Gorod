package com.makecity.core.extenstion

import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.makecity.core.R
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
				groups[url] = ExecutableRange(indexFrom, indexFrom + match.length)
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

fun TextView.textWithExecutable(lines: List<String>, onExecute: (String) -> Unit) {
	val separator = ", "
	val groups = mutableMapOf<String, ExecutableRange>()
	val result = lines.joinToString(separator)

	var currentIndex = 0

	lines.forEachIndexed { index, text ->
		val tempIndex = currentIndex + text.length
		groups[text] = ExecutableRange(currentIndex, tempIndex)
		currentIndex = tempIndex

		val isLastIndex = index.inc() == lines.size

		if (!isLastIndex) {
			currentIndex += separator.length
		}
	}

	val spannableBuilder = SpannableStringBuilder(result)
	groups.forEach { entry ->
		spannableBuilder.setSpan(object : ClickableSpan() {
			override fun onClick(widget: View) {
				onExecute(entry.key)
			}

			override fun updateDrawState(ds: TextPaint) {
				ds.color = ContextCompat.getColor(context, R.color.colorAccent)
				ds.isUnderlineText = true
			}
		}, entry.value.from, entry.value.to, 0)
	}
	text = spannableBuilder
}