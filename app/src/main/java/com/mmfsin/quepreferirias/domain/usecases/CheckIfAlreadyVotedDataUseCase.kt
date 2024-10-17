package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import javax.inject.Inject

class CheckIfAlreadyVotedDataUseCase @Inject constructor(
    private val dilemmasRepo: IDilemmasRepository,
    private val dualismsRepo: IDualismsRepository
) : BaseUseCase<CheckIfAlreadyVotedDataUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? {
        return when (params.type) {
            DILEMMA -> dilemmasRepo.alreadyDilemmaVoted(params.dataId)
            DUALISM -> dualismsRepo.alreadyDualismVoted(params.dataId)
        }
    }

    data class Params(
        val dataId: String,
        val type: DashboardType
    )
}