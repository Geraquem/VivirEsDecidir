package com.mmfsin.quepreferirias.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.quepreferirias.data.models.QuestionDTO
import com.mmfsin.quepreferirias.data.models.UserNameDTO
import com.mmfsin.quepreferirias.domain.interfaces.IQuestionsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRealmDatabase
import com.mmfsin.quepreferirias.utils.SEND_QUESTIONS_ROOT
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class QuestionsRepository @Inject constructor(
    private val realmDatabase: IRealmDatabase
) : IQuestionsRepository {

    private val reference = Firebase.database.reference.child(SEND_QUESTIONS_ROOT)

    override suspend fun sendQuestion(question: QuestionDTO): Boolean {
        var result = false
        val latch = CountDownLatch(1)
        val key = UUID.randomUUID().toString()
        reference.child(key).setValue(question).addOnCompleteListener {
            result = it.isSuccessful
            latch.countDown()
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        return result
    }

    override fun getCreatorName(): String? {
        val names = realmDatabase.getObjectsFromRealm { where<UserNameDTO>().findAll() }
        return if (names.isEmpty()) null else names.first().name
    }

    override fun saveCreatorName(name: String) {
        val userName = UserNameDTO(name = name)
        realmDatabase.addObject { userName }
    }
}