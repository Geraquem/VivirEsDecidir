package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SendDualismDTO(
    @PrimaryKey
    var dualismId: String = "",
    var txtTop: String = "",
    var txtBottom: String = "",
    var creatorId: String = "",
    var creatorName: String = "",
    var timestamp: Long = 0,
    var filterValue: Double = 0.0
) : RealmObject()