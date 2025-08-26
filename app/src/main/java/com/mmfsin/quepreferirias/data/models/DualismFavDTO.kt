package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DualismFavDTO : RealmObject {
    @PrimaryKey
    var dualismId: String = ""
    var txtTop: String = ""
    var txtBottom: String = ""
}