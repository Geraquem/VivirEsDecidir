package com.mmfsin.quepreferirias.data.mappers

import com.mmfsin.quepreferirias.data.models.QuestionDTO

fun toQuestionDTO(textTop: String, textBottom: String, creatorName: String) = QuestionDTO(
    textTop = textTop,
    textBottom = textBottom,
    creatorName = creatorName.ifEmpty { null }
)