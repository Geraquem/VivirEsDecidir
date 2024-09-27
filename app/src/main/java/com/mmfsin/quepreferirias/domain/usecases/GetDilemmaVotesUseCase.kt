package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaVotes
import javax.inject.Inject

class GetDilemmaVotesUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<GetDilemmaVotesUseCase.Params, DilemmaVotes?>() {

    override suspend fun execute(params: Params): DilemmaVotes? =
        repository.getDilemmaVotes(params.dilemmaId)

    data class Params(
        val dilemmaId: String
    )
}