package com.mmfsin.quepreferirias.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toDualismFavList
import com.mmfsin.quepreferirias.data.mappers.toSession
import com.mmfsin.quepreferirias.data.models.DualismFavDTO
import com.mmfsin.quepreferirias.data.models.DualismVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.data.models.SessionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.domain.models.Session
import com.mmfsin.quepreferirias.utils.CREATOR_ID
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.DUALISMS
import com.mmfsin.quepreferirias.utils.DUALISMS_SENT
import com.mmfsin.quepreferirias.utils.DUALISM_ID
import com.mmfsin.quepreferirias.utils.EXPLANATION
import com.mmfsin.quepreferirias.utils.FILTER_VALUE
import com.mmfsin.quepreferirias.utils.REPORTED
import com.mmfsin.quepreferirias.utils.SAVED_DUALISMS
import com.mmfsin.quepreferirias.utils.SERVER_SAVED_DUALISMS
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.USERS
import com.mmfsin.quepreferirias.utils.VOTES_BOTTOM
import com.mmfsin.quepreferirias.utils.VOTES_TOP
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DualismsRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val realmDatabase: IRealmDatabase
) : IDualismsRepository {

    private val reference = Firebase.database.reference

    private fun getSession(): Session? {
        val session =
            realmDatabase.getObjectsFromRealm { where<SessionDTO>().findAll() }
        return if (session.isEmpty()) null else session.first().toSession()
    }

    override suspend fun getDualisms(): List<Dualism> {
        val latch = CountDownLatch(1)
        val db = FirebaseFirestore.getInstance()
//        val randomValue = Math.random()
        val randomValue = 0.0001
        val totalLimit = 2L
        val finalDataList = mutableListOf<Dualism>()

        db.collection(DUALISMS)
            .whereGreaterThan(FILTER_VALUE, randomValue)
            .limit(totalLimit)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() < totalLimit) {
                    db.collection(DUALISMS)
                        .whereLessThan(FILTER_VALUE, randomValue)
                        .limit(totalLimit - documents.size())
                        .get()
                        .addOnSuccessListener { moreDocuments ->
                            for (moreDoc in moreDocuments) {
                                val moreData = Dualism(
                                    id = moreDoc.id,
                                    explanation = moreDoc.getString(EXPLANATION),
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
                        val data = Dualism(
                            id = doc.id,
                            explanation = doc.getString(EXPLANATION),
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

    override suspend fun checkIfDualismIsFav(dualismId: String): Boolean {
        val dualisms = getFavDualisms()
        return dualisms.any { it.dualismId == dualismId }
    }

    override suspend fun getFavDualisms(): List<DualismFav> {
        val session = getSession()
        val latch = CountDownLatch(1)
        return session?.let {
            val sharedPrefs =
                context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            if (sharedPrefs.getBoolean(SERVER_SAVED_DUALISMS, true)) {
                realmDatabase.deleteAllObjects(DualismFavDTO::class.java)
                val dualisms = mutableListOf<DualismFavDTO>()
                Firebase.firestore.collection(USERS).document(session.id)
                    .collection(SAVED_DUALISMS).get().addOnSuccessListener { d ->
                        for (document in d.documents) {
                            try {
                                document.toObject(DualismFavDTO::class.java)
                                    ?.let { favDualism ->
                                        dualisms.add(favDualism)
                                        realmDatabase.addObject { favDualism }
                                    }
                            } catch (e: Exception) {
                                Log.e("error", "error parsing dualism fav")
                            }
                        }
                        latch.countDown()
                    }.addOnFailureListener { latch.countDown() }

                withContext(Dispatchers.IO) { latch.await() }

                sharedPrefs.edit().apply {
                    putBoolean(SERVER_SAVED_DUALISMS, false)
                    apply()
                }
                dualisms.toDualismFavList().reversed()
            } else {
                val dualisms =
                    realmDatabase.getObjectsFromRealm { where<DualismFavDTO>().findAll() }
                dualisms.toDualismFavList().reversed()
            }
        } ?: run { emptyList() }
    }

    override suspend fun getDualismVotes(dualismId: String): DualismVotes? {
        val latch = CountDownLatch(1)
        var votes: DualismVotes? = null
        val root = reference.child(DUALISMS).child(dualismId)
        root.get().addOnCompleteListener { dataSnapshot ->
            val result = dataSnapshot.result
            if (result.exists()) {
                votes = DualismVotes(
                    votesTop = result.child(VOTES_TOP).childrenCount,
                    votesBottom = result.child(VOTES_BOTTOM).childrenCount,
                )
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) { latch.await() }
        return votes
    }

    override suspend fun voteDualism(dualismId: String, isTop: Boolean, voted: DualismVotedDTO) {
        val latch = CountDownLatch(1)
        val secondChild = if (isTop) VOTES_TOP else VOTES_BOTTOM
        reference.child(DUALISMS).child(dualismId).child(secondChild)
            .updateChildren(mapOf(UUID.randomUUID().toString() to isTop)).addOnCompleteListener {
                it.isSuccessful
                realmDatabase.addObject { voted }
                latch.countDown()
            }

        withContext(Dispatchers.IO) { latch.await() }
    }

    override suspend fun sendDualism(dualism: SendDualismDTO) {
        val latch = CountDownLatch(3)

        /** Set in User Dualisms */
        Firebase.firestore.collection(USERS).document(dualism.creatorId)
            .collection(DUALISMS_SENT).document(dualism.dualismId)
            .set(dualism, SetOptions.merge())
            .addOnCompleteListener {
                realmDatabase.addObject { dualism }
                latch.countDown()
            }


        /** Set in total dualisms */
        Firebase.firestore.collection(DUALISMS).document(dualism.dualismId)
            .set(dualism, SetOptions.merge())
            .addOnCompleteListener {
                latch.countDown()
            }

        /** Set in Realtime for votes */
        val root = reference.child(DUALISMS).child(dualism.dualismId)
        root.setValue(dualism.dualismId).addOnSuccessListener {
            latch.countDown()
        }

        withContext(Dispatchers.IO) { latch.await() }
    }

    override suspend fun setFavDualism(dualism: DualismFavDTO) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let {
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(SAVED_DUALISMS).document(dualism.dualismId)
                .set(dualism, SetOptions.merge())
                .addOnCompleteListener {
                    realmDatabase.addObject { dualism }
                    latch.countDown()
                }
            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun deleteFavDualism(dualismId: String) {
        val session = getSession()
        val latch = CountDownLatch(1)
        session?.let {
            Firebase.firestore.collection(USERS).document(session.id)
                .collection(SAVED_DUALISMS).document(dualismId)
                .delete().addOnCompleteListener {
                    realmDatabase.deleteObject(
                        DualismFavDTO::class.java,
                        DUALISM_ID,
                        dualismId
                    )
                    latch.countDown()
                }
            withContext(Dispatchers.IO) { latch.await() }
        }
    }

    override suspend fun reportDualism(dualism: Dualism) {
        val latch = CountDownLatch(1)
        Firebase.firestore.collection(REPORTED)
            .document(DUALISMS).collection(dualism.id)
            .document(dualism.id).set(dualism)
            .addOnCompleteListener {
                val a = 2
                latch.countDown()
            }
        withContext(Dispatchers.IO) { latch.await() }
    }
}