package com.mmfsin.quepreferirias.presentation.create

import com.mmfsin.quepreferirias.data.repository.QuestionsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IQuestions
import com.mmfsin.quepreferirias.domain.models.QuestionSentDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SendQuestionsPresenter(private val view: SendQuestionView) : IQuestions, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { QuestionsRepository(this) }

    fun sendQuestion(question: QuestionSentDTO) =
        launch(Dispatchers.IO) { repository.sendQuestion(question) }

    override fun result(result: Boolean) {
        launch { view.result(result) }
    }
}