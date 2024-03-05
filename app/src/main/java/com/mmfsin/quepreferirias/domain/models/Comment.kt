package com.mmfsin.quepreferirias.domain.models

open class Comment(
    val userId: String,
    val name: String,
    val comment: String,
    val image: String,
    val date: String,
    val likes: Long
)