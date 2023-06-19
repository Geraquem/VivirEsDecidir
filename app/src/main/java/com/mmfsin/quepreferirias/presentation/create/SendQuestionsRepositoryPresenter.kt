package com.mmfsin.quepreferirias.presentation.create

import com.mmfsin.quepreferirias.data.repository.QuestionsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import com.mmfsin.quepreferirias.data.models.QuestionSentDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SendQuestionsRepositoryPresenter(private val view: SendQuestionView) : IQuestionsRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

//    private val repository by lazy { QuestionsRepository(this) }

    fun sendQuestion(question: QuestionSentDTO) {}
//        launch(Dispatchers.IO) { repository.sendQuestion(question) }

    override fun result(result: Boolean) {
        launch { view.result(result) }
    }
}