package com.mmfsin.quepreferirias.domain.models

open class SendDualism(
    var dualismId: String,
    var explanation: String?,
    var txtTop: String,
    var txtBottom: String,
    var creatorId: String,
    var creatorName: String,
    var timestamp: Long,
    var filterValue: Double
)