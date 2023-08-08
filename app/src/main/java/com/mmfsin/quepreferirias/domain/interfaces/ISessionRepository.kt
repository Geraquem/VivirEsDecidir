package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.SessionDTO

interface ISessionRepository {
    suspend fun logIn(session: SessionDTO)
}