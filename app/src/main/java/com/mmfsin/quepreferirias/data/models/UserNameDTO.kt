package com.mmfsin.quepreferirias.data.models

import com.mmfsin.quepreferirias.utils.CREATOR_NAME_ID
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserNameDTO(
    @PrimaryKey
    var id: Int = CREATOR_NAME_ID,
    var name: String = ""
) : RealmObject()