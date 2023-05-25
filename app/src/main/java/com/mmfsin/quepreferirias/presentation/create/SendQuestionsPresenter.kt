package com.mmfsin.quepreferirias.presentation.create

import com.mmfsin.quepreferirias.data.repository.FirebaseRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRepository
import com.mmfsin.quepreferirias.domain.models.DataDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SendQuestionsPresenter(private val view: SendQuestionView) : IRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { FirebaseRepository(this) }

    override fun getDataFromFirebase(dataList: List<DataDTO>) {}

    override fun somethingWentWrong() {}
}