package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.SavedData

interface IDataRepository {
    /** DILEMMAS */
    suspend fun getDilemmas(): List<Dilemma>
    suspend fun getDataGivenKeyList(idList: List<String>): List<SavedData>
    suspend fun getSavedDataInRealm(): List<SavedData>
    suspend fun vote(dataId: String, voteId: String)
}