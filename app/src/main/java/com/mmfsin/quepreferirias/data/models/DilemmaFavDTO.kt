package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DilemmaFavDTO : RealmObject {
    @PrimaryKey
    var dilemmaId: String = ""
    var txtTop: String = ""
    var txtBottom: String = ""
}