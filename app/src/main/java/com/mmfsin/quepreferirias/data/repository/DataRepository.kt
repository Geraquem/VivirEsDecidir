package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toData
import com.mmfsin.quepreferirias.data.mappers.toSavedDataList
import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.data.models.SavedDataDTO
import com.mmfsin.quepreferirias.data.models.SavedDataIdDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.utils.*
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : IDataRepository {

    private val reference = Firebase.database.reference
    private val fReference = Firebase.firestore

    override suspend fun getConditionalData(): List<ConditionalData> {
        val latch = CountDownLatch(1)
        val conditionalDataList = mutableListOf<ConditionalData>()


        fReference.collection(USERS).document(email)
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





//        reference.get().addOnSuccessListener { root ->
//            val allData = root.child(PRUEBAS_ROOT)
//            for (child in allData.children) {
//                child.getValue(DataDTO::class.java)?.let { item ->
//                    val votesYes = child.child(YES).childrenCount
//                    val votesNo = child.child(NO).childrenCount
//                    child.key?.let { id -> conditionalDataList.add(item.toData(id, votesYes, votesNo)) }
//                }
//            }
//            latch.countDown()
//        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return conditionalDataList//.shuffled()
    }

    override suspend fun vote(dataId: String, voteId: String) {
        Firebase.database.reference.child(PRUEBAS_ROOT).child(dataId).child(voteId).push()
            .setValue(true)
    }

    override suspend fun getDataGivenKeyList(idList: List<String>): List<SavedData> {
        val latch = CountDownLatch(1)
        val dataList = mutableListOf<SavedDataDTO>()

        val collectionReference = Firebase.firestore.collection(BUT_DATA)
        collectionReference.whereIn(DATA_ID, idList).get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                val savedDataDTO = document.toObject(SavedDataDTO::class.java)
                dataList.add(savedDataDTO)
                realmDatabase.addObject { savedDataDTO }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return dataList.toSavedDataList()
    }

    override suspend fun getSavedDataInRealm(): List<SavedData> {
        val dataList = realmDatabase.getObjectsFromRealm { where<SavedDataDTO>().findAll() }
        return dataList.toSavedDataList()
    }
}
