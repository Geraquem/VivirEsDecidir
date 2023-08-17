package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toData
import com.mmfsin.quepreferirias.data.mappers.toSavedDataList
import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.data.models.SavedDataDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.domain.models.Data
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

    private val reference = Firebase.database.reference.child(PRUEBAS_ROOT)

    override suspend fun getData(): List<Data> {
        val latch = CountDownLatch(1)
        val dataList = mutableListOf<Data>()
        reference.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(DataDTO::class.java)?.let { item ->
                    val votesYes = child.child(YES).childrenCount
                    val votesNo = child.child(NO).childrenCount
                    child.key?.let { id -> dataList.add(item.toData(id, votesYes, votesNo)) }
                }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return dataList//.shuffled()
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
                val dataDTO = document.toObject(SavedDataDTO::class.java)
                dataList.add(dataDTO)
                realmDatabase.addObject { dataDTO }
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
