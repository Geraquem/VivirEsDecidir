package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CommentReplyDTO : RealmObject {
    @PrimaryKey
    var replyId: String = ""
    var commentId: String = ""
    var userId: String = ""
    var userName: String = ""
    var image: String = ""
    var reply: String = ""
    var likes: Long = 0
    var votedUp: Boolean = false
    var votedDown: Boolean = false
}