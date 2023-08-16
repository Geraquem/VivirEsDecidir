package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Session

interface ISessionRepository {
    fun saveSession(session: Session)
    fun deleteSession()
    fun getSession(): Session?
    suspend fun checkIfIsSavedData(dataId: String): Boolean?
    suspend fun saveDataToUser(dataId: String): Boolean?
}