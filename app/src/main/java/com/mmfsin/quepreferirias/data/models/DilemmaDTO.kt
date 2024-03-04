package com.mmfsin.quepreferirias.data.models

open class DilemmaDTO(
    var txtTop: String = "",
    var txtBottom: String = "",
    var creatorName: String? = null,
    var votesYes: Long? = null,
    var votesNo: Long? = null,
    var comments: Long? = null,
)