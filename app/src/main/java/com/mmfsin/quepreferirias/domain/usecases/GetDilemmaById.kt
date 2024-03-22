package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.Dilemma
import javax.inject.Inject

class GetDilemmaById @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCase<GetDilemmaById.Params, Dilemma?>() {

    override suspend fun execute(params: Params): Dilemma? =
        repository.getDilemmaById(params.dilemmaId)

    data class Params(
        val dilemmaId: String
    )
}