package com.mmfsin.quepreferirias.presentation

import com.mmfsin.quepreferirias.data.database.RealmDatabase
import com.mmfsin.quepreferirias.data.repository.FirebaseRepository
import com.mmfsin.quepreferirias.domain.interfaces.IRepository
import com.mmfsin.quepreferirias.domain.models.DataDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainView) : IRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repository by lazy { FirebaseRepository(this) }
    private val realm by lazy { RealmDatabase() }

    fun getData() = launch(Dispatchers.IO) { repository.getDataFromFirebase() }

    fun calculatePercent(votesYes: Long, votesNo: Long): Pair<String, String> {
        val total = votesYes + votesNo

        val percentYes = votesYes * 100.0f / total
        val percentNo = votesNo * 100.0f / total

        var strYes = ""
        var strNo = ""
        if (percentYes.toString().length >= 5 && percentNo.toString().length >= 5) {
            strYes = percentYes.toString().substring(0, 5) + "%"
            strNo = percentNo.toString().substring(0, 5) + "%"
        } else {
            strYes = "$percentYes%"
            strNo = "$percentNo%"
        }

        return Pair(strYes, strNo)
    }

    override fun getDataFromFirebase(dataList: List<DataDTO>) {
//        dataList.shuffled().forEach{data -> realm.addObject { data } }
        launch { view.firebaseReady(dataList.shuffled()) }
    }

    override fun somethingWentWrong() {
    }
}