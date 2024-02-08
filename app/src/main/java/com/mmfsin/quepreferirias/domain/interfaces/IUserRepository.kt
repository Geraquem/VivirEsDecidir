package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Session

interface IUserRepository {
    suspend fun saveSession(session: Session): Boolean
    fun deleteSession()
    fun getSession(): Session?

    suspend fun checkIfIsSavedData(dataId: String): Boolean?
    suspend fun saveData(dataId: String): Boolean?
    suspend fun getSavedDataKeys(email: String): List<String>
}