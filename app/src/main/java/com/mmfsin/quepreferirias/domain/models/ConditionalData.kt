package com.mmfsin.quepreferirias.domain.models

open class ConditionalData(
    var id: String = "",
    var topText: String = "",
    var bottomText: String = "",
    var votesYes: Long = 0L,
    var votesNo: Long = 0L,
    var creatorName: String? = null,
)