package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toDilemmaFavList
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.mappers.toSessionDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.COMMENT_LIKES
import com.mmfsin.quepreferirias.utils.DILEMMA_ID
import com.mmfsin.quepreferirias.utils.INSTAGRAM
import com.mmfsin.quepreferirias.utils.SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.TIKTOK
import com.mmfsin.quepreferirias.utils.TWITTER
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import com.mmfsin.quepreferirias.utils.UPDATE_USER_DATA
import com.mmfsin.quepreferirias.utils.USERS
import com.mmfsin.quepreferirias.utils.YOUTUBE
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

    private fun getSession(): Session? {
        val session = realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
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

    override suspend fun getUserSession(): Session? {
        val session = getSession()
        var newSession: SessionDTO? = null
        val latch = CountDownLatch(1)
        return session?.let {
            val sharedPrefs = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            if (sharedPrefs.getBoolean(UPDATE_USER_DATA, true)) {
                realmDatabase.deleteAllObjects(SessionDTO::class.java)
                Firebase.firestore.collection(USERS).document(session.id).get()
                    .addOnSuccessListener { d ->
                        try {
                            newSession = d.toObject(SessionDTO::class.java)
                            newSession?.let { session -> realmDatabase.addObject { session } }
                        } catch (e: Exception) {
                            Log.e("error", "error parsing user data")
                        }
                        latch.countDown()
                    }.addOnFailureListener {
                        latch.countDown()
                    }
                withContext(Dispatchers.IO) { latch.await() }
                sharedPrefs.edit().apply {
                    putBoolean(UPDATE_USER_DATA, false)
                    apply()
                }
                newSession?.toSession()
            } else session
        } ?: run { null }
    }

    override fun deleteSession() = realmDatabase.deleteAllData()

    override suspend fun updateProfile(rrss: RRSS) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let { user ->
            val documentReference = Firebase.firestore.collection(USERS).document(user.id)
            val updatedRRSS = hashMapOf<String, Any>()
            rrss.instagram?.let { updatedRRSS.put(INSTAGRAM, it) }
            rrss.twitter?.let { updatedRRSS.put(TWITTER, it) }
            rrss.tiktok?.let { updatedRRSS.put(TIKTOK, it) }
            rrss.youtube?.let { updatedRRSS.put(YOUTUBE, it) }
            documentReference.update(updatedRRSS).addOnCompleteListener {
                if (it.isSuccessful) {
                    user.rrss = rrss
                    realmDatabase.addObject { user.toSessionDTO() }
                    latch.countDown()
                }
            }
        }
        withContext(Dispatchers.IO) { latch.await() }
    }
}
