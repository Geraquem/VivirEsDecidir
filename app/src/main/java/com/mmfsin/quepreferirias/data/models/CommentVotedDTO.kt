package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CommentVotedDTO(
    @PrimaryKey
    var commentId: String = "",
    var votedUp: Boolean = false
) : RealmObject()