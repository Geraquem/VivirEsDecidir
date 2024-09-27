package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import javax.inject.Inject

class VoteDilemmaUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<VoteDilemmaUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) =
        repository.voteDilemma(params.dilemmaId, params.isYes)

    data class Params(
        val dilemmaId: String,
        val isYes: Boolean
    )
}