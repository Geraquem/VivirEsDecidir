package com.mmfsin.quepreferirias.data.models

open class DataDTO(
    var id: String = "",
    var textA: String = "",
    var textB: String = "",
    var votesA: Long = 0L,
    var votesB: Long = 0L,
    var creatorName: String? = null,
)