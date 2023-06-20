package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import javax.inject.Inject

class SendQuestionUseCase @Inject constructor(private val repository: IDataRepository) :
    BaseUseCase<SendQuestionUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        return true
    }

    data class Params(
        val textTop: String,
        val textBottom: String,
        val creatorName: String? = null
    )
}