package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.QuestionDTO

interface IQuestionsRepository {
    fun getCreatorName(): String?
    fun saveCreatorName(name: String)
    suspend fun sendQuestion(question: QuestionDTO): Boolean
}