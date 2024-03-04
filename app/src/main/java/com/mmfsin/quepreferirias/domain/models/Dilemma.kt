package com.mmfsin.quepreferirias.domain.models

open class Dilemma(
    var id: String = "",
    var topText: String = "",
    var bottomText: String = "",
    var votesYes: Long = 0,
    var votesNo: Long = 0,
    var creatorName: String? = null,
)