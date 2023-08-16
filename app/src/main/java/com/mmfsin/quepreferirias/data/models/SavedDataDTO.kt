package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SavedDataDTO(
    @PrimaryKey
    var dataId: String = "",
) : RealmObject()