package com.mmfsin.quepreferirias.domain.models

import com.mmfsin.quepreferirias.R
import com.mmfsin.quepreferirias.domain.models.RRSSType.*

open class Session(
    var id: String = "",
    var imageUrl: String = "",
    var email: String = "",
    var name: String = "",
    var fullName: String = "",
    var rrss: RRSS? = null
)

open class RRSS(
    var instagram: String? = null,
    var twitter: String? = null,
    var tiktok: String? = null,
    var youtube: String? = null,
)

enum class RRSSType {
    INSTAGRAM,
    TWITTER,
    TIKTOK,
    YOUTUBE
}

fun RRSSType.getRRSSIcon(): Int {
    return when (this) {
        INSTAGRAM -> R.drawable.ic_rrss_instagram
        TWITTER -> R.drawable.ic_rrss_x
        TIKTOK -> R.drawable.ic_rrss_tiktok
        YOUTUBE -> R.drawable.ic_rrss_youtube
    }
}