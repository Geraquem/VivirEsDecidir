package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.mappers.toSessionDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.DATA
import com.mmfsin.quepreferirias.utils.DATA_SAVED
import com.mmfsin.quepreferirias.utils.USERS
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : ISessionRepository {

    override fun saveSession(session: Session) {
        realmDatabase.addObject { session.toSessionDTO() }
        saveInFirestore(session)
    }

    private fun saveInFirestore(session: Session) {
        Firebase.firestore.collection(USERS).document(session.email).set(session)
    }

    override fun getSession(): Session? {
        val session = realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }

    override fun deleteSession() = realmDatabase.deleteAllData()

    override suspend fun saveDataToUser(dataId: String): Boolean? {
        val session = getSession() ?: return null
        var result: Boolean? = null
        val data = hashMapOf(dataId to true)
        val latch = CountDownLatch(1)
        Firebase.firestore.collection(USERS).document(session.email)
            .collection(DATA).document(DATA_SAVED)
            .set(data, SetOptions.merge())
            .addOnCompleteListener {
                result = it.isSuccessful
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
        return result
    }
}
