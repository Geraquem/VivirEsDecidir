package com.mmfsin.quepreferirias.presentation.sendquestions

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.SendQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendQuestionsViewModel @Inject constructor(
    private val sendQuestionUseCase: SendQuestionUseCase
) : BaseViewModel<SendQuestionsEvent>() {

    fun getCreatorName(){

    }

    fun sendQuestion(textTop: String, textBottom: String, creatorName: String) {
        executeUseCase(
            {
                sendQuestionUseCase.execute(
                    SendQuestionUseCase.Params(textTop, textBottom, creatorName)
                )
            },
            { result -> _event.value = SendQuestionsEvent.SendQuestionResult(result) },
            { _event.value = SendQuestionsEvent.SWW })
    }
}