package com.mmfsin.quepreferirias.domain.models

open class Comment(
    val commentId: String,
    val userId: String,
    val name: String,
    val comment: String,
    val image: String,
    val since: Int,
    var likes: Long,
    var votedUp: Boolean,
    var votedDown: Boolean,
)