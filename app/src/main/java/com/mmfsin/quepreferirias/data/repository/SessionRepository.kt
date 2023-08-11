package com.mmfsin.quepreferirias.data.repository

import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.mappers.toSessionDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import com.mmfsin.quepreferirias.domain.models.Session
import io.realm.kotlin.where
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ISessionRepository {

    override fun saveSession(session: Session) {
        realmDatabase.addObject { session.toSessionDTO() }
    }

    override fun getSession(): Session? {
        val session = realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }
}
