package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.mappers.toSessionDTO
import com.mmfsin.quepreferirias.data.models.SavedDataIdDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.DATA_SAVED
import com.mmfsin.quepreferirias.utils.SAVED
import com.mmfsin.quepreferirias.utils.SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import com.mmfsin.quepreferirias.utils.USERS
import com.mmfsin.quepreferirias.utils.USER_DATA
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

    private val reference = Firebase.database.reference

    override suspend fun saveSession(session: Session): Boolean {
        return if (savedInFirebase(session)) {
            realmDatabase.addObject { session.toSessionDTO() }
            true
        } else false
    }

    private suspend fun savedInFirebase(session: Session): Boolean {
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

    private suspend fun retrieveSavedData(): List<SavedDataIdDTO>? {
        val session = getSession() ?: return null
        var savedData = realmDatabase.getObjectsFromRealm { where<SavedDataIdDTO>().findAll() }

        val sharedPrefs = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val update = sharedPrefs.getBoolean(UPDATE_SAVED_DATA, true)
        /** if true goes to Firebase */
        if (update) savedData = getSavedDataFromFirebase(session.email)
        /** set update to false */
        sharedPrefs.edit().apply {
            putBoolean(UPDATE_SAVED_DATA, false)
            apply()
        }
        return savedData
    }

    override suspend fun checkIfIsSavedData(dataId: String): Boolean? {
        val savedData = retrieveSavedData()
        savedData?.let { item ->
            item.forEach { if (it.dataId == dataId) return true }
            return false
        } ?: run { return null }
    }

    private suspend fun getSavedDataFromFirebase(email: String): List<SavedDataIdDTO> {
        val data = mutableListOf<SavedDataIdDTO>()
        val latch = CountDownLatch(1)
        Firebase.firestore.collection(USERS).document(email)
            .collection(USER_DATA).document(DATA_SAVED)
            .get().addOnCompleteListener {
                val arrayList = it.result.data?.keys?.let { it1 -> ArrayList(it1) }
                arrayList?.forEach { id ->
                    val savedDataIdDTO = SavedDataIdDTO(dataId = id)
                    data.add(savedDataIdDTO)
                    realmDatabase.addObject { savedDataIdDTO }
                }
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
        return data
    }

    override suspend fun getSavedDataKeys(email: String): List<String> {
        val savedData = retrieveSavedData()
        val data = mutableListOf<String>()
        savedData?.forEach { data.add(it.dataId) }
        return data
    }
}
