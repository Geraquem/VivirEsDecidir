package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.Session

interface IUserRepository {
    suspend fun saveSession(session: Session): Boolean
    fun deleteSession()
    suspend fun getUserSession(): Session?

    suspend fun updateProfile(rrss: RRSS)
}