package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toDualismVotedDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.DualismVoted
import javax.inject.Inject

class VoteDualismUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCase<VoteDualismUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val votedDilemma = DualismVoted(
            dualismId = params.dualismId,
            votedTop = params.isTop
        )
        repository.voteDualism(params.dualismId, params.isTop, votedDilemma.toDualismVotedDTO())
    }

    data class Params(
        val dualismId: String,
        val isTop: Boolean
    )
}