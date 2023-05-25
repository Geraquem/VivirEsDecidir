package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.SEND_QUESTIONS_ROOT
import com.mmfsin.quepreferirias.domain.interfaces.IQuestions
import com.mmfsin.quepreferirias.domain.models.QuestionSentDTO
import java.util.*

class QuestionsRepository(private val listener: IQuestions) {

    fun sendQuestion(question: QuestionSentDTO) {
        val key = UUID.randomUUID().toString()
        Firebase.database.reference.child(SEND_QUESTIONS_ROOT).child(key).setValue(question)
            .addOnCompleteListener { listener.result(it.isSuccessful) }
    }
}