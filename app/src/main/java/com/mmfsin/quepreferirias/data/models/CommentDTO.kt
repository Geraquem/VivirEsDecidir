package com.mmfsin.quepreferirias.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CommentDTO(
    @PrimaryKey
    var commentId: String = "",
    var userId: String = "",
    var name: String = "",
    var comment: String = "",
    var image: String = "",
    var timestamp: String = "",
    var date: String = "",
    var likes: Long = 0,
    var votedUp: Boolean = false,
    var votedDown: Boolean = false
) : RealmObject()