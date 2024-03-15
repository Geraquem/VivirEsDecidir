package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import javax.inject.Inject

class CheckIfDilemmaIsFavUseCase @Inject constructor(
    val repository: IDilemmasRepository
) : BaseUseCase<CheckIfDilemmaIsFavUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean =
        repository.checkIsDilemmaIsFav(params.dilemmaId)

    data class Params(
        val dilemmaId: String
    )
}