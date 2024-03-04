package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.Dilemma
import javax.inject.Inject

class GetDilemmas @Inject constructor(private val repository: IDataRepository) :
    BaseUseCaseNoParams<List<Dilemma>>() {

    override suspend fun execute(): List<Dilemma> = repository.getDilemmas()//.shuffled()
}