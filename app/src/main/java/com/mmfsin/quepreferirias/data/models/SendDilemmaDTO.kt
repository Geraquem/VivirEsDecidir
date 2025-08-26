package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class SendDilemmaDTO : RealmObject {
    @PrimaryKey
    var dilemmaId: String = ""
    var txtTop: String = ""
    var txtBottom: String = ""
    var creatorId: String = ""
    var creatorName: String = ""
    var timestamp: Long = 0
    var filterValue: Double = 0.0
}