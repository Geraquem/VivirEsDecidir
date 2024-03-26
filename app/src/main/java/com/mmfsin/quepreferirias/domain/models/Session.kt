package com.mmfsin.quepreferirias.domain.models

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