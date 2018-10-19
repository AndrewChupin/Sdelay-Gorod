package com.makecity.core.extenstion

import android.os.Bundle
import android.support.v4.app.Fragment


inline fun <T: Fragment> T.withArguments(closure: Bundle.() -> Unit): T {
	val bundle = Bundle()
	bundle.closure()
	arguments = bundle
	return this
}
