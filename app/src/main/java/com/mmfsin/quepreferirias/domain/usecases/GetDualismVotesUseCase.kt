package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.DualismVotes
import javax.inject.Inject

class GetDualismVotesUseCase @Inject constructor(
    private val repository: IDualismsRepository
) : BaseUseCase<GetDualismVotesUseCase.Params, DualismVotes?>() {

    override suspend fun execute(params: Params): DualismVotes? =
        repository.getDualismVotes(params.dualismId)

    data class Params(
        val dualismId: String
    )
}