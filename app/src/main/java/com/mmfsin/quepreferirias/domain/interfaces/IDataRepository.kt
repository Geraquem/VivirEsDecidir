package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Data
import com.mmfsin.quepreferirias.domain.models.SavedData

interface IDataRepository {
    suspend fun getData(): List<Data>
    suspend fun getDataGivenKeyList(idList: List<String>): List<SavedData>
    suspend fun getSavedDataInRealm(): List<SavedData>
    suspend fun vote(dataId: String, voteId: String)
}