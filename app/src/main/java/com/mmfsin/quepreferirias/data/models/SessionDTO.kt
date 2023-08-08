package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SessionDTO(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var initiated: Boolean = false,
) : RealmObject()