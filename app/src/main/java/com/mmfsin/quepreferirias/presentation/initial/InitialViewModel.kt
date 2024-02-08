package com.mmfsin.quepreferirias.presentation.initial

import com.mmfsin.quepreferirias.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
) : BaseViewModel<InitialEvent>() {

//    fun getAppData() {
//        executeUseCase(
//            { getAppDataUseCase.execute() },
//            { result ->
//                _event.value =
//                    if (result.isEmpty()) DashboardEvent.SWW else DashboardEvent.AppData(result)
//            },
//            { _event.value = DashboardEvent.SWW }
//        )
//    }l
}