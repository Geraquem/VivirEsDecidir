package com.mmfsin.quepreferirias.domain.interfaces

import com.mmfsin.quepreferirias.domain.models.DataDTO

interface IData {
    fun getDataFromFirebase(dataList: List<DataDTO>)
    fun somethingWentWrong()
}