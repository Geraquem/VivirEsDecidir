package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Session

interface ISessionRepository {
    fun saveSession(session: Session)
    fun getSession(): Session?
}