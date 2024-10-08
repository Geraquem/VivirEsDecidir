package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.DilemmaFavDTO
import com.mmfsin.quepreferirias.data.models.DilemmaVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDilemmaDTO
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import com.mmfsin.quepreferirias.domain.models.SendDilemma

interface IDilemmasRepository {
    suspend fun getDilemmas(): List<Dilemma>
    suspend fun getDilemmaById(dilemmaId: String): Dilemma?

    suspend fun getDilemmaVotes(dilemmaId: String): DilemmaVotes?
    suspend fun voteDilemma(dilemmaId: String, isYes: Boolean, voted: DilemmaVotedDTO)
    suspend fun alreadyDilemmaVoted(dilemmaId: String): Boolean?

    suspend fun getFavDilemmas(): List<DilemmaFav>
    suspend fun setFavDilemma(dilemma: DilemmaFavDTO)
    suspend fun deleteFavDilemma(dilemmaId: String)
    suspend fun checkIfDilemmaIsFav(dilemmaId: String): Boolean

    suspend fun sendDilemma(dilemma: SendDilemmaDTO)
    suspend fun getMyDilemmas(): List<SendDilemma>
    suspend fun deleteMyDilemma(dilemmaId: String)

    suspend fun getOtherUserDilemmas(userId: String): List<SendDilemma>

    suspend fun reportDilemma(dilemma: Dilemma)
}