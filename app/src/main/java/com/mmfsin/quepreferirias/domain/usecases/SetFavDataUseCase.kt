package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toDilemmaFavDTO
import com.mmfsin.quepreferirias.data.mappers.toDualismFavDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.domain.models.DualismFav
import com.mmfsin.quepreferirias.presentation.models.DashboardType
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SetFavDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val dilemmasRepo: IDilemmasRepository,
    private val dualismsRepo: IDualismsRepository
) : BaseUseCase<SetFavDataUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        when (params.type) {
            DILEMMA -> setFavDilemma(params)
            DUALISM -> setFavDualism(params)
        }
    }

    private suspend fun setFavDilemma(params: Params) {
        val dilemmaFav = DilemmaFav(
            dilemmaId = params.dataId,
            txtTop = params.txtTop,
            txtBottom = params.txtBottom,
        )
        dilemmasRepo.setFavDilemma(dilemmaFav.toDilemmaFavDTO())
    }

    private suspend fun setFavDualism(params: Params) {
        val dualismFav = DualismFav(
            dualismId = params.dataId,
            txtTop = params.txtTop,
            txtBottom = params.txtBottom,
        )
        dualismsRepo.setFavDualism(dualismFav.toDualismFavDTO())
    }

    data class Params(
        val dataId: String,
        val type: DashboardType,
        val txtTop: String,
        val txtBottom: String
    )
}