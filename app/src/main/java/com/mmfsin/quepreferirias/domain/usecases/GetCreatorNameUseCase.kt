package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import javax.inject.Inject

class GetCreatorNameUseCase @Inject constructor(private val repository: IQuestionsRepository) :
    BaseUseCaseNoParams<String?>() {

    override suspend fun execute(): String? = repository.getCreatorName()
}