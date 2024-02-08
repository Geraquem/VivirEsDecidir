package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.ConditionalData
import com.mmfsin.quepreferirias.domain.models.SavedData

interface IDataRepository {
    /** CONDITIONALS */
    suspend fun getConditionalData(): List<ConditionalData>
    suspend fun getDataGivenKeyList(idList: List<String>): List<SavedData>
    suspend fun getSavedDataInRealm(): List<SavedData>
    suspend fun vote(dataId: String, voteId: String)


}