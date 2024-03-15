package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import javax.inject.Inject

class GetFavDilemmasUseCase @Inject constructor(
    private val dilemmasRepository: IDilemmasRepository
) : BaseUseCaseNoParams<List<DilemmaFav>>() {

    override suspend fun execute(): List<DilemmaFav> = dilemmasRepository.getFavDilemmas()
}