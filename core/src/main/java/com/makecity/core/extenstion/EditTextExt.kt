package com.makecity.core.extenstion

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

inline fun EditText.addOnTextChangeListener(
    crossinline onTextChanged: (CharSequence?) -> Unit
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            onTextChanged(p0)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}
