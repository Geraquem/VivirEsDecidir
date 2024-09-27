package com.mmfsin.quepreferirias.domain.models

open class SendDilemma(
    var dilemmaId: String,
    var txtTop: String,
    var txtBottom: String,
    var creatorId: String,
    var creatorName: String,
    var timestamp: Long,
    var filterValue: Double
)