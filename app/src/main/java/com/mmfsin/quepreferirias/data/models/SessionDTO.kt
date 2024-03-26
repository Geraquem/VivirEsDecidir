package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SessionDTO(
    @PrimaryKey
    var id: String = "",
    var imageUrl: String = "",
    var email: String = "",
    var name: String = "",
    var fullName: String = "",
    var instagram: String? = null,
    var twitter: String? = null,
    var tiktok: String? = null,
    var youtube: String? = null
) : RealmObject()