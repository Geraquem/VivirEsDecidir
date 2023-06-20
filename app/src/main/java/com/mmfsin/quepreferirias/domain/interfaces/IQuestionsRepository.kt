package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.QuestionDTO

interface IQuestionsRepository {
    suspend fun sendQuestion(question: QuestionDTO): Boolean
    fun saveCreatorName(name: String)
}