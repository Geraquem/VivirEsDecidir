package com.mmfsin.quepreferirias.utils

import android.text.Editable
import androidx.fragment.app.FragmentActivity
import com.mmfsin.quepreferirias.base.dialog.ErrorDialog

fun FragmentActivity.showErrorDialog(action: () -> Unit) {
    val dialog = ErrorDialog(action)
    this.let { dialog.show(it.supportFragmentManager, "") }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)