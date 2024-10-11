package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeleteMyDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val dilemmasRepo: IDilemmasRepository,
    private val dualismsRepo: IDualismsRepository
) : BaseUseCase<DeleteMyDataUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        when (params.type) {
            DILEMMA -> dilemmasRepo.deleteMyDilemma(params.dataId)
            DUALISM -> dualismsRepo.deleteMyDualism(params.dataId)
        }
    }

    data class Params(
        val dataId: String,
        val type: DashboardType
    )
}