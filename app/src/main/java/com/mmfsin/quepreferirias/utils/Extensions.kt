package com.mmfsin.quepreferirias.utils

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.CountDownTimer
import android.text.Editable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.base.dialog.ErrorDialog
import com.mmfsin.quepreferirias.domain.models.RRSSType
import com.mmfsin.quepreferirias.domain.models.RRSSType.*
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

fun FragmentActivity.changeBgColor(background: Drawable, color: Int) {
    if (background is GradientDrawable) {
        background.setColor(ContextCompat.getColor(this, color))
    }
}

fun FragmentActivity.openRRSS(type: RRSSType, name: String) {
    val url = when (type) {
        RRSSType.INSTAGRAM -> getString(R.string.instagram_url, name)
        RRSSType.TWITTER -> getString(R.string.twitter_url, name)
        RRSSType.TIKTOK -> getString(R.string.tiktok_url, name)
        RRSSType.YOUTUBE -> getString(R.string.youtube_url, name)
    }
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun <T1 : Any, T2 : Any, R : Any> checkNotNulls(p1: T1?, p2: T2?, block: (T1, T2) -> R): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}