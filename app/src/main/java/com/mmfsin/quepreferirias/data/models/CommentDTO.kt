package com.mmfsin.quepreferirias.data.models

open class CommentDTO(
    val commentId: String = "",
    val userId: String = "",
    val name: String = "",
    val comment: String = "",
    val image: String = "",
    val timestamp: String = "",
    val date: String = "",
    val likes: Long = 0
)