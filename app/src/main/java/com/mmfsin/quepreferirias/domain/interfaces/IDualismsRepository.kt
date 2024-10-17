package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.DualismFavDTO
import com.mmfsin.quepreferirias.data.models.DualismVotedDTO
import com.mmfsin.quepreferirias.data.models.SendDualismDTO
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import com.mmfsin.quepreferirias.domain.models.SendDualism

interface IDualismsRepository {
    suspend fun getDualisms(): List<Dualism>
    suspend fun getDualismById(dualismId: String): Dualism?

    suspend fun getDualismVotes(dualismId: String): DualismVotes?
    suspend fun voteDualism(dualismId: String, isTop: Boolean, voted: DualismVotedDTO)
    suspend fun alreadyDualismVoted(dualismId: String): Boolean?

    suspend fun getFavDualisms(): List<DualismFav>
    suspend fun setFavDualism(dualism: DualismFavDTO)
    suspend fun deleteFavDualism(dualismId: String)
    suspend fun checkIfDualismIsFav(dualismId: String): Boolean

    suspend fun sendDualism(dualism: SendDualismDTO)
    suspend fun getMyDualisms(): List<SendDualism>
    suspend fun deleteMyDualism(dualismId: String)

    suspend fun getOtherUserDualisms(userId: String): List<SendDualism>

    suspend fun reportDualism(dualism: Dualism)
}