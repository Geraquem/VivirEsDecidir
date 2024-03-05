package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.Comment
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.SavedData

interface IDataRepository {
    /** DILEMMAS */
    suspend fun getDilemmas(): List<Dilemma>
    suspend fun getDilemmaComments(dilemmaId: String): List<Comment>
    suspend fun setDilemmaComment(dilemmaId: String, comment: Comment): Boolean
}