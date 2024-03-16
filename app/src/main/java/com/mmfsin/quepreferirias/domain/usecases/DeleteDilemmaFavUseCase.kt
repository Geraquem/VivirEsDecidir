package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeleteDilemmaFavUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: IDilemmasRepository
) : BaseUseCase<DeleteDilemmaFavUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) = repository.deleteFavDilemma(params.dilemmaId)

    data class Params(
        val dilemmaId: String
    )
}