package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.models.SendDilemma
import javax.inject.Inject

class GetMyDilemmasUseCase @Inject constructor(
    private val repository: IDilemmasRepository
) : BaseUseCaseNoParams<List<SendDilemma>>() {

    override suspend fun execute(): List<SendDilemma> = repository.getMyDilemmas()
}