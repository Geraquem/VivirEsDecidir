package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.mappers.toData
import com.mmfsin.quepreferirias.data.models.DataDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.utils.VOTE_NO
import com.mmfsin.quepreferirias.utils.DILEMMAS
import com.mmfsin.quepreferirias.utils.VOTE_YES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class DataRepository @Inject constructor() : IDataRepository {

    private val reference = Firebase.database.reference.child(DILEMMAS)

    override suspend fun getDataFromFirebase(): List<Data> {
        val latch = CountDownLatch(1)
        val dataList = mutableListOf<Data>()
        reference.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(DataDTO::class.java)?.let { item ->
                    val votesYes = child.child(VOTE_YES).childrenCount
                    val votesNo = child.child(VOTE_NO).childrenCount
                    child.key?.let { id -> dataList.add(item.toData(id, votesYes, votesNo)) }
                }
            }
            latch.countDown()
        }.addOnFailureListener { latch.countDown() }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return dataList.shuffled()
    }


    override suspend fun vote(dataId: String, voteId: String) {
        Firebase.database.reference.child(DILEMMAS).child(dataId).child(voteId).push()
            .setValue(true)
    }
}
