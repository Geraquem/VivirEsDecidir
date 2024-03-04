package com.mmfsin.quepreferirias.domain.interfaces

interface IQuestionsRepository {
    fun getCreatorName(): String?
    fun saveCreatorName(name: String)
}