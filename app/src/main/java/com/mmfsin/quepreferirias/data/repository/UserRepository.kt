package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.mappers.toSessionDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.USERS
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : IUserRepository {

    override suspend fun saveSession(session: Session): Boolean {
        return if (saveSessionInFirebase(session)) {
            realmDatabase.addObject { session.toSessionDTO() }
            true
        } else false
    }

    private suspend fun saveSessionInFirebase(session: Session): Boolean {
        val latch = CountDownLatch(1)
        var result = false
        Firebase.firestore.collection(USERS).document(session.id)
            .set(session, SetOptions.merge())
            .addOnCompleteListener {
                result = it.isSuccessful
                latch.countDown()
            }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override fun getSession(): Session? {
        val session = realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }

    override fun deleteSession() = realmDatabase.deleteAllData()
}
