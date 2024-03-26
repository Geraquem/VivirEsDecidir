package com.mmfsin.quepreferirias.utils

import android.os.CountDownTimer
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import com.mmfsin.quepreferirias.base.dialog.ErrorDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun FragmentActivity.showErrorDialog(action: () -> Unit) {
    val dialog = ErrorDialog(action)
    this.let { dialog.show(it.supportFragmentManager, "") }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun countDown(duration: Long, action: () -> Unit) {
    object : CountDownTimer(duration, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            action()
        }
    }.start()
}

fun Long.getDateTime(): String? {
    return try {
        val sdf = SimpleDateFormat("dd / MM / yyyy", Locale.ROOT)
        val netDate = Date(this * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        null
    }
}

fun Long.timestampToDate(): String {
    val dateFormat = SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun String.checkIfEmpty(): String? {
    return this.ifBlank { null }
}