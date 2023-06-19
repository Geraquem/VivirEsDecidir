package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.data.models.DataDTO

interface IDataRepository {
    fun getDataFromFirebase(dataList: List<DataDTO>)
    fun somethingWentWrong()
}