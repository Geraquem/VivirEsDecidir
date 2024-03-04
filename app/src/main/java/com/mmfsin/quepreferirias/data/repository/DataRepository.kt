package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.CREATOR_NAME
import com.mmfsin.quepreferirias.utils.TXT_BOTTOM
import com.mmfsin.quepreferirias.utils.TXT_TOP
import com.mmfsin.quepreferirias.utils.VOTES_NO
import com.mmfsin.quepreferirias.utils.VOTES_YES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : IDataRepository {

    private val reference = Firebase.database.reference

    override suspend fun getDilemmas(): List<Dilemma> {
        val latch = CountDownLatch(1)
        val dilemmaList = mutableListOf<Dilemma>()
        val root = reference.child(DILEMMAS)
        root.get().addOnCompleteListener { dataSnapshot ->
            for (child in dataSnapshot.result.children) {
                if (child.exists()) {
                    val id = child.key ?: child.ref.key
                    val textTop = child.child(TXT_TOP).value.toString()
                    val textBottom = child.child(TXT_BOTTOM).value.toString()
                    val votesYes = child.child(VOTES_YES).childrenCount
                    val votesNo = child.child(VOTES_NO).childrenCount
                    val creator = child.child(CREATOR_NAME).value?.toString()
                    val data = Dilemma(
                        id.toString(),
                        textTop,
                        textBottom,
                        votesYes,
                        votesNo,
                        creator
                    )
                    dilemmaList.add(data)
                }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO)
        {
            latch.await()
        }
        return dilemmaList//.shuffled()
    }

    override suspend fun vote(dataId: String, voteId: String) {
//        Firebase.database.reference.child(PRUEBAS_ROOT).child(dataId).child(voteId).push()
//            .setValue(true)
    }

    override suspend fun getDataGivenKeyList(idList: List<String>): List<SavedData> {
//        val latch = CountDownLatch(1)
//        val dataList = mutableListOf<SavedDataDTO>()
//
//        val collectionReference = Firebase.firestore.collection(CONDITIONAL_DATA)
//        collectionReference.whereIn(DATA_ID, idList).get().addOnSuccessListener { querySnapshot ->
//            for (document in querySnapshot) {
//                val savedDataDTO = document.toObject(SavedDataDTO::class.java)
//                dataList.add(savedDataDTO)
//                realmDatabase.addObject { savedDataDTO }
//            }
//            latch.countDown()
//        }.addOnFailureListener { latch.countDown() }
//
//        withContext(Dispatchers.IO) {
//            latch.await()
//        }
//        return dataList.toSavedDataList()
        return emptyList()
    }

    override suspend fun getSavedDataInRealm(): List<SavedData> {
//        val dataList = realmDatabase.getObjectsFromRealm { where<SavedDataDTO>().findAll() }
//        return dataList.toSavedDataList()
        return emptyList()
    }
}
