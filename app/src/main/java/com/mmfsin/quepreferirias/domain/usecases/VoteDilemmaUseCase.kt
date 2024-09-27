package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toDilemmaVotedDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaVoted
import javax.inject.Inject

class VoteDilemmaUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<VoteDilemmaUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val votedDilemma = DilemmaVoted(
            dilemmaId = params.dilemmaId,
            votedYes = params.isYes
        )
        repository.voteDilemma(params.dilemmaId, params.isYes, votedDilemma.toDilemmaVotedDTO())
    }

    data class Params(
        val dilemmaId: String,
        val isYes: Boolean
    )
}