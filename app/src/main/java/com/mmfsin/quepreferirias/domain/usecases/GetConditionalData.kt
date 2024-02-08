package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.ConditionalData
import javax.inject.Inject

class GetConditionalData @Inject constructor(private val repository: IDataRepository) :
    BaseUseCaseNoParams<List<ConditionalData>>() {

    override suspend fun execute(): List<ConditionalData> = repository.getConditionalData()//.shuffled()
}