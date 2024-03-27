package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DilemmaVotedDTO(
    @PrimaryKey
    var dilemmaId: String = "",
    var votedOk: Boolean = false
) : RealmObject()