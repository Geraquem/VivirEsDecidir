package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCase
import com.mmfsin.quepreferirias.data.mappers.toQuestionDTO
import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import javax.inject.Inject

class SendQuestionUseCase @Inject constructor(private val repository: IQuestionsRepository) :
    BaseUseCase<SendQuestionUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val name = params.creatorName
        if (name.isNotEmpty()) repository.saveCreatorName(name)
        val question = toQuestionDTO(params.textTop, params.textBottom, name)
        return repository.sendQuestion(question)
    }

    data class Params(
        val textTop: String,
        val textBottom: String,
        val creatorName: String
    )
}