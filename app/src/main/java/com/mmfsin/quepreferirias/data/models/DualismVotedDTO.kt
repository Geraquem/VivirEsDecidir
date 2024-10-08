package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DualismVotedDTO(
    @PrimaryKey
    var dualismId: String = "",
    var votedTop: Boolean = false
) : RealmObject()