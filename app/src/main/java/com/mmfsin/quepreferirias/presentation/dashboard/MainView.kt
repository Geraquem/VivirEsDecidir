package com.mmfsin.quepreferirias.presentation.dashboard

import com.mmfsin.quepreferirias.domain.models.DataDTO

interface MainView {
    fun firebaseReady(dataList: List<DataDTO>)
    fun setSingleData(data: DataDTO)
}