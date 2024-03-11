package com.mmfsin.quepreferirias.domain.models

open class Comment(
    val commentId: String,
    val userId: String,
    val name: String,
    val comment: String,
    val image: String,
    val since: Int,
    val likes: Long,
    val coloredUp: Boolean,
    val coloredDown: Boolean,
)