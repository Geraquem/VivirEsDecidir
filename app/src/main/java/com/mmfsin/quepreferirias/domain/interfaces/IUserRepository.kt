package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.Session

interface IUserRepository {
    suspend fun saveSession(session: Session): Boolean
    suspend fun getUserSession(): Session?
    fun deleteSession()

    suspend fun updateProfile(rrss: RRSS)

    suspend fun getUserById(userId: String): Session?
}