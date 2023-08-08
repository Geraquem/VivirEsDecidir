package com.mmfsin.quepreferirias.data.repository

import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import io.realm.kotlin.where
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ISessionRepository {

    override suspend fun logIn(session: SessionDTO) {
        val data = realmDatabase.getObjectsFromRealm {
            where<SessionDTO>().equalTo("id", session.id).findAll()
        }
        val userData = if (data.isEmpty()) session else data.first()
        userData.initiated = true
        realmDatabase.addObject { userData }
    }

}
