package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Data

interface IDataRepository {
    suspend fun getDataFromFirebase(): List<Data>
    suspend fun vote(dataId: String, voteId: String)
}