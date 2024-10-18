package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toDilemma
import com.mmfsin.quepreferirias.data.mappers.toDilemmaFavList
import com.mmfsin.quepreferirias.data.mappers.toSendDilemmaList
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.models.DilemmaDTO
import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.CREATOR_ID
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.DILEMMAS_SENT
import com.mmfsin.quepreferirias.utils.DILEMMA_ID
import com.mmfsin.quepreferirias.utils.FILTER_VALUE
import com.mmfsin.quepreferirias.utils.REPORTED
import com.mmfsin.quepreferirias.utils.SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SERVER_SAVED_DILEMMAS
import com.mmfsin.quepreferirias.utils.SERVER_SENT_DILEMMAS
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.USERS
import com.mmfsin.quepreferirias.utils.VOTES_NO
import com.mmfsin.quepreferirias.utils.VOTES_YES
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DilemmasRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : IDilemmasRepository {

    private val reference = Firebase.database.reference

    override suspend fun getDilemmas(): List<Dilemma> {
        val latch = CountDownLatch(1)
        val db = FirebaseFirestore.getInstance()
//        val randomValue = Math.random()
        val randomValue = 0.0001
        val totalLimit = 2L
        val finalDataList = mutableListOf<Dilemma>()

        db.collection(DILEMMAS)
            .whereGreaterThan(FILTER_VALUE, randomValue)
            .limit(totalLimit)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() < totalLimit) {
                    db.collection(DILEMMAS)
                        .whereLessThan(FILTER_VALUE, randomValue)
                        .limit(totalLimit - documents.size())
                        .get()
                        .addOnSuccessListener { moreDocuments ->
                            for (moreDoc in moreDocuments) {
                                val moreData = Dilemma(
                                    id = moreDoc.id,
                                    txtTop = moreDoc.getString(TXT_TOP) ?: "",
                                    txtBottom = moreDoc.getString(TXT_BOTTOM) ?: "",
                                    creatorId = moreDoc.getString(CREATOR_ID),
                                    creatorName = moreDoc.getString(CREATOR_NAME)
                                )
                                finalDataList.add(moreData)
                            }
                        }
                } else {
                    for (doc in documents) {
                        val data = Dilemma(
                            id = doc.id,
                            txtTop = doc.getString(TXT_TOP) ?: "",
                            txtBottom = doc.getString(TXT_BOTTOM) ?: "",
                            creatorId = doc.getString(CREATOR_ID),
                            creatorName = doc.getString(CREATOR_NAME)
                        )
                        finalDataList.add(data)
                    }
                }
                latch.countDown()
            }

        withContext(Dispatchers.IO) { latch.await() }

        return finalDataList
    }

    override suspend fun getDilemmaById(dilemmaId: String): Dilemma? {
        val latch = CountDownLatch(1)
        var dilemma: DilemmaDTO? = null
        Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
            .get().addOnSuccessListener { d ->
                try {
                    d.toObject(DilemmaDTO::class.java)?.let {
                        dilemma = it
                    }
                } catch (e: Exception) {
                    Log.e("error", "error parsing comment")
                }
                latch.countDown()
            }.addOnFailureListener {
                latch.countDown()
            }

        withContext(Dispatchers.IO) { latch.await() }
        return dilemma?.toDilemma()
    }

    override suspend fun getDilemmaVotes(dilemmaId: String): DilemmaVotes? {
        val latch = CountDownLatch(1)
        var votes: DilemmaVotes? = null
        val root = reference.child(DILEMMAS).child(dilemmaId)
        root.get().addOnCompleteListener { dataSnapshot ->
            val result = dataSnapshot.result
            if (result.exists()) {
                votes = DilemmaVotes(
                    votesYes = result.child(VOTES_YES).childrenCount,
                    votesNo = result.child(VOTES_NO).childrenCount,
                )
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) { latch.await() }
        return votes
    }

    override suspend fun voteDilemma(dilemmaId: String, isYes: Boolean, voted: DilemmaVotedDTO) {
        val latch = CountDownLatch(1)
        val secondChild = if (isYes) VOTES_YES else VOTES_NO
        reference.child(DILEMMAS).child(dilemmaId).child(secondChild)
            .updateChildren(mapOf(UUID.randomUUID().toString() to isYes)).addOnCompleteListener {
                it.isSuccessful
                realmDatabase.addObject { voted }
                latch.countDown()
            }

        withContext(Dispatchers.IO) { latch.await() }
    }

    override suspend fun alreadyDilemmaVoted(dilemmaId: String): Boolean? {
        val voted = realmDatabase.getObjectFromRealm(
            DilemmaVotedDTO::class.java,
            DILEMMA_ID,
            dilemmaId
        )
        return voted?.votedYes
    }

    private fun getSession(): Session? {
        val session =
            realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }

    override suspend fun setFavDilemma(dilemma: DilemmaFavDTO) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let {
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(SAVED_DILEMMAS).document(dilemma.dilemmaId)
                .set(dilemma, SetOptions.merge())
                .addOnCompleteListener {
                    realmDatabase.addObject { dilemma }
                    latch.countDown()
                }
            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun getFavDilemmas(): List<DilemmaFav> {
        val session = getSession()
        val latch = CountDownLatch(1)
        return session?.let {
            val sharedPrefs =
                context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            if (sharedPrefs.getBoolean(SERVER_SAVED_DILEMMAS, true)) {
                realmDatabase.deleteAllObjects(DilemmaFavDTO::class.java)
                val dilemmas = mutableListOf<DilemmaFavDTO>()
                Firebase.firestore.collection(USERS).document(session.id)
                    .collection(SAVED_DILEMMAS).get().addOnSuccessListener { d ->
                        for (document in d.documents) {
                            try {
                                document.toObject(DilemmaFavDTO::class.java)
                                    ?.let { favDilemma ->
                                        dilemmas.add(favDilemma)
                                        realmDatabase.addObject { favDilemma }
                                    }
                            } catch (e: Exception) {
                                Log.e("error", "error parsing dilemma fav")
                            }
                        }
                        latch.countDown()
                    }.addOnFailureListener {
                        latch.countDown()
                    }
                withContext(Dispatchers.IO) { latch.await() }
                sharedPrefs.edit().apply {
                    putBoolean(SERVER_SAVED_DILEMMAS, false)
                    apply()
                }
                dilemmas.toDilemmaFavList().reversed()
            } else {
                val dilemmas =
                    realmDatabase.getObjectsFromRealm { where<DilemmaFavDTO>().findAll() }
                dilemmas.toDilemmaFavList().reversed()
            }
        } ?: run { emptyList() }
    }

    override suspend fun checkIfDilemmaIsFav(dilemmaId: String): Boolean {
        val dilemmas = getFavDilemmas()
        return dilemmas.any { it.dilemmaId == dilemmaId }
    }

    override suspend fun deleteFavDilemma(dilemmaId: String) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let {
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(SAVED_DILEMMAS).document(dilemmaId)
                .delete().addOnCompleteListener {
                    realmDatabase.deleteObject(
                        DilemmaFavDTO::class.java,
                        DILEMMA_ID,
                        dilemmaId
                    )
                    latch.countDown()
                }
            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun sendDilemma(dilemma: SendDilemmaDTO) {
        val latch = CountDownLatch(3)

        /** Set in User Dilemmas */
        Firebase.firestore.collection(USERS).document(dilemma.creatorId)
            .collection(DILEMMAS_SENT).document(dilemma.dilemmaId)
            .set(dilemma, SetOptions.merge())
            .addOnCompleteListener {
                realmDatabase.addObject { dilemma }
                latch.countDown()
            }


        /** Set in total dilemmas */
        Firebase.firestore.collection(DILEMMAS).document(dilemma.dilemmaId)
            .set(dilemma, SetOptions.merge())
            .addOnCompleteListener {
                latch.countDown()
            }

        /** Set in Realtime for votes */
        val root = reference.child(DILEMMAS).child(dilemma.dilemmaId)
        root.setValue(dilemma.dilemmaId).addOnSuccessListener {
            latch.countDown()
        }

        withContext(Dispatchers.IO) { latch.await() }
    }

    override suspend fun getMyDilemmas(): List<SendDilemma> {
        val session = getSession()
        val latch = CountDownLatch(1)
        return session?.let {
            val sharedPrefs =
                context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            if (sharedPrefs.getBoolean(SERVER_SENT_DILEMMAS, true)) {
                realmDatabase.deleteAllObjects(SendDilemmaDTO::class.java)
                val dilemmas = mutableListOf<SendDilemmaDTO>()
                Firebase.firestore.collection(USERS).document(session.id)
                    .collection(DILEMMAS_SENT).get().addOnSuccessListener { d ->
                        for (document in d.documents) {
                            try {
                                document.toObject(SendDilemmaDTO::class.java)
                                    ?.let { sentDilemma ->
                                        dilemmas.add(sentDilemma)
                                        realmDatabase.addObject { sentDilemma }
                                    }
                            } catch (e: Exception) {
                                Log.e("error", "error parsing sent dilemma")
                            }
                        }
                        latch.countDown()
                    }.addOnFailureListener {
                        latch.countDown()
                    }
                withContext(Dispatchers.IO) { latch.await() }
                sharedPrefs.edit().apply {
                    putBoolean(SERVER_SENT_DILEMMAS, false)
                    apply()
                }
                dilemmas.toSendDilemmaList().reversed()
            } else {
                val dilemmas =
                    realmDatabase.getObjectsFromRealm { where<SendDilemmaDTO>().findAll() }
                dilemmas.toSendDilemmaList().reversed()
            }
        } ?: run { emptyList() }
    }

    override suspend fun getOtherUserDilemmas(userId: String): List<SendDilemma> {
        val latch = CountDownLatch(1)
        val dilemmas = mutableListOf<SendDilemmaDTO>()
        Firebase.firestore.collection(USERS).document(userId)
            .collection(DILEMMAS_SENT).get().addOnSuccessListener { d ->
                for (document in d.documents) {
                    try {
                        document.toObject(SendDilemmaDTO::class.java)?.let { sentDilemma ->
                            dilemmas.add(sentDilemma)
                        }
                    } catch (e: Exception) {
                        Log.e("error", "error parsing sent dilemma")
                    }
                }
                latch.countDown()
            }.addOnFailureListener {
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
        return dilemmas.toSendDilemmaList().reversed()
    }

    override suspend fun deleteMyDilemma(dilemmaId: String) {
        val session = getSession()
        val latch = CountDownLatch(3)
        session?.let {
            /** Delete user dilemma */
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(DILEMMAS_SENT).document(dilemmaId)
                .delete().addOnCompleteListener {
                    realmDatabase.deleteObject(
                        SendDilemmaDTO::class.java,
                        DILEMMA_ID,
                        dilemmaId
                    )
                    latch.countDown()
                }

            /** Delete in total dilemmas */
            Firebase.firestore.collection(DILEMMAS).document(dilemmaId)
                .delete().addOnCompleteListener {
                    realmDatabase.deleteObject(
                        DilemmaFavDTO::class.java,
                        DILEMMA_ID,
                        dilemmaId
                    )
                    latch.countDown()
                }

            /** Delete in Realmtime and votes */
            val root = reference.child(DILEMMAS).child(dilemmaId)
            root.removeValue().addOnCompleteListener {
                latch.countDown()
            }

            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun reportDilemma(dilemma: Dilemma) {
        val latch = CountDownLatch(1)
        Firebase.firestore.collection(REPORTED)
            .document(DILEMMAS).collection(dilemma.id)
            .document(dilemma.id).set(dilemma)
            .addOnCompleteListener {
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
    }
}
