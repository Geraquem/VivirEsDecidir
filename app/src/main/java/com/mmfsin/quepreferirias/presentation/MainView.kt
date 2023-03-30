package com.mmfsin.quepreferirias.presentation

import com.mmfsin.quepreferirias.domain.models.DataDTO

interface MainView {
    fun firebaseReady(dataList: List<DataDTO>)
    fun setSingleData(data: DataDTO)
}