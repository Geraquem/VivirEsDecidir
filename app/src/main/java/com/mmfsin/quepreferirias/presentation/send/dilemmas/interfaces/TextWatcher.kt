package com.mmfsin.quepreferirias.presentation.send.dilemmas.interfaces

import android.text.Editable
import android.widget.EditText

class TextWatcher {

    fun addTextWatcher(editText: EditText, listener: TextWatcherListener) {
        editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { listener.onTextChanged(s.length.toString()) }
            }
        })
    }

    interface TextWatcherListener {
        fun onTextChanged(text: String)
    }
}