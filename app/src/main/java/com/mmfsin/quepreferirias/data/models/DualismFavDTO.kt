package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DualismFavDTO(
    @PrimaryKey
    var dualismId: String = "",
    var txtTop: String = "",
    var txtBottom: String = ""
) : RealmObject()