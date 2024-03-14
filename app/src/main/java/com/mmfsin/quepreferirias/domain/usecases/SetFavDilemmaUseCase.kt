package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toDilemmaFavDTO
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.SESSION_INITIATED
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SetFavDilemmaUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: IDataRepository
) : BaseUseCase<SetFavDilemmaUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val dilemmaFav = DilemmaFav(
            dilemmaId = params.dilemmaId,
            txtTop = params.txtTop,
            txtBottom = params.txtBottom,
        )
        sessionRepository.setFavDilemma(dilemmaFav.toDilemmaFavDTO())
    }

    data class Params(
        val dilemmaId: String,
        val txtTop: String,
        val txtBottom: String
    )
}