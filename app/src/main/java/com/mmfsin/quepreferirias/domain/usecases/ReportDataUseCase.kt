package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import javax.inject.Inject

class ReportDataUseCase @Inject constructor(
    private val dilemmasRepo: IDilemmasRepository,
    private val dualismsRepo: IDualismsRepository
) : BaseUseCase<ReportDataUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        when (params.type) {
            DILEMMA -> dilemmasRepo.reportDilemma(params.data as Dilemma)
//            DUALISMS -> dualismsRepo.reportDualism(params.data as Dualism)
            else -> {}
        }
    }

    data class Params(
        val data: Any,
        val type: DashboardType
    )
}