package com.mmfsin.quepreferirias.data.repository

import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import javax.inject.Inject

class QuestionsRepository@Inject constructor(): IQuestionsRepository {

    override fun result(result: Boolean) {
        TODO("Not yet implemented")
    }

//    fun sendQuestion(question: QuestionSentDTO) {
//        val key = UUID.randomUUID().toString()
//        Firebase.database.reference.child(SEND_QUESTIONS_ROOT).child(key).setValue(question)
//            .addOnCompleteListener { /**listener.result(it.isSuccessful)*/ }
//    }
}