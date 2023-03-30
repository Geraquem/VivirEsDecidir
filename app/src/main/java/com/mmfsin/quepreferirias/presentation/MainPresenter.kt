package com.mmfsin.quepreferirias.presentation

import com.mmfsin.quepreferirias.data.database.RealmDatabase
import com.mmfsin.quepreferirias.data.repository.FirebaseRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRepository
import com.mmfsin.quepreferirias.domain.models.DataDTO
import io.realm.RealmModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainView) : IRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { FirebaseRepository(this) }
    private val realm by lazy { RealmDatabase() }

    fun getData() = launch(Dispatchers.IO) { repository.getDataFromFirebase() }


    override fun getDataFromFirebase(dataList: List<DataDTO>) {
//        dataList.shuffled().forEach{data -> realm.addObject { data } }
        launch { view.firebaseReady(dataList.shuffled()) }
    }

    override fun somethingWentWrong() {
    }
}