package com.mmfsin.quepreferirias.presentation

import com.mmfsin.quepreferirias.data.models.DataDTO

interface MainView {
    fun firebaseReady(dataList: List<DataDTO>)
    fun setSingleData(data: DataDTO)
}