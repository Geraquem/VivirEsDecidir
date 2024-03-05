package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.CommentDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Session

fun Session.toSessionDTO() = SessionDTO(
    id = id,
    imageUrl = imageUrl,
    email = email,
    name = name,
    fullName = fullName
)