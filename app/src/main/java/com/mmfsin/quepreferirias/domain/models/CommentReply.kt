package com.mmfsin.quepreferirias.domain.models

open class CommentReply(
    val replyId: String,
    val commentId: String,
    val userId: String,
    val userName: String,
    val image: String,
    val reply: String,
    var likes: Long,
    var votedUp: Boolean,
    var votedDown: Boolean,
)