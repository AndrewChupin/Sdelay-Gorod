package com.makecity.core.extenstion


inline fun Any?.isNullValue(closure: () -> Unit) {
    if (this == null) {
        closure()
    }
}
