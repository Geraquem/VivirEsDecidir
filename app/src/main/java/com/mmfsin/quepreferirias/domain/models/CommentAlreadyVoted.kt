package com.mmfsin.quepreferirias.domain.models

data class CommentAlreadyVoted(
    val alreadyVoted: Boolean,
    val hasVotedTheSame: Boolean
)