package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CommentVotedDTO : RealmObject {
    @PrimaryKey
    var commentId: String = ""
    var votedUp: Boolean = false
}