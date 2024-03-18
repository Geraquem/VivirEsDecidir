package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SendDilemmaDTO(
    @PrimaryKey
    var dilemmaId: String = "",
    var txtTop: String = "",
    var txtBottom: String = "",
    var creatorId: String = "",
    var creatorName: String = ""
) : RealmObject()