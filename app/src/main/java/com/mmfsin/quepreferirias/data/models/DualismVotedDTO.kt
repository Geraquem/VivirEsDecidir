package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DualismVotedDTO : RealmObject {
    @PrimaryKey
    var dualismId: String = ""
    var votedTop: Boolean = false
}