package com.mmfsin.quepreferirias.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataDTO(
    @PrimaryKey
    var id: String = "",
    var textA: String = "",
    var textB: String = "",
    var votesA: Long = 0L,
    var votesB: Long = 0L,
) : RealmObject()