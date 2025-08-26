package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DilemmaVotedDTO : RealmObject {
    @PrimaryKey
    var dilemmaId: String = ""
    var votedYes: Boolean = false
}