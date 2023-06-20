package com.mmfsin.quepreferirias.presentation.sendquestions

sealed class SendQuestionsEvent {
    class SendQuestionResult(val result: Boolean) : SendQuestionsEvent()
    object SWW : SendQuestionsEvent()
}