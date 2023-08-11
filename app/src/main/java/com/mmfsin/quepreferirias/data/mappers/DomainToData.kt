package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.QuestionDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.models.Session

fun toQuestionDTO(textTop: String, textBottom: String, creatorName: String) = QuestionDTO(
    textTop = textTop,
    textBottom = textBottom,
    creatorName = creatorName.ifEmpty { null }
)

fun Session.toSessionDTO() = SessionDTO(email, name)