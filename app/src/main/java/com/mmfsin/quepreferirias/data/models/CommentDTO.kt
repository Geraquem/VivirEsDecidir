package com.mmfsin.quepreferirias.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CommentDTO : RealmObject {
    @PrimaryKey
    var commentId: String = ""
    var userId: String = ""
    var name: String = ""
    var comment: String = ""
    var image: String = ""
    var timestamp: Long = 0
    var date: String = ""
    var likes: Long = 0
    var votedUp: Boolean = false
    var votedDown: Boolean = false
//    var replies: List<CommentReplyDTO> = emptyList()
}