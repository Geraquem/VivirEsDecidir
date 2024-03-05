package com.mmfsin.quepreferirias.data.models

open class CommentDTO(
    val userId: String = "",
    val name: String = "",
    val comment: String = "",
    val image: String = "",
    val date: String = "",
    val likes: Long = 0
)