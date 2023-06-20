package com.mmfsin.quepreferirias.presentation.sendquestions

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetCreatorNameUseCase
import com.mmfsin.quepreferirias.domain.usecases.SendQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendQuestionsViewModel @Inject constructor(
    private val getCreatorNameUseCase: GetCreatorNameUseCase,
    private val sendQuestionUseCase: SendQuestionUseCase
) : BaseViewModel<SendQuestionsEvent>() {

    fun getCreatorName(){
        executeUseCase(
            {
                getCreatorNameUseCase.execute()
            },
            { result -> _event.value = SendQuestionsEvent.CreatorName(result) },
            { _event.value = SendQuestionsEvent.SWW })
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