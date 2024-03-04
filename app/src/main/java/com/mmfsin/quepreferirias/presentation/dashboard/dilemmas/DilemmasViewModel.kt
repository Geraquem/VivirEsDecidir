package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmasViewModel @Inject constructor(
    private val getDilemmas: GetDilemmas,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val userVoteUseCase: UserVoteUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val checkIfAlreadySavedUseCase: CheckIfAlreadySavedUseCase,
    private val saveDataUseCase: SaveDataUseCase
) : BaseViewModel<DilemmasEvent>() {

    fun getConditionalData() {
        executeUseCase(
            { getDilemmas.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) DilemmasEvent.SWW else DilemmasEvent.Data(result)
            },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getPercents(votesYes: Long, votesNo: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesYes, votesNo)) },
            { result ->
                _event.value =
                    result?.let { DilemmasEvent.GetPercents(it) }
                        ?: run { DilemmasEvent.SWW }
            },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun vote(dataId: String, isYes: Boolean) {
        executeUseCase(
            { userVoteUseCase.execute(UserVoteUseCase.Params(dataId, isYes)) },
            { Log.i("userVoteUseCase: ", "User voted successfully") },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getComments(dataId: String) {
        executeUseCase(
            { getDilemmaCommentsUseCase.execute(GetDilemmaCommentsUseCase.Params(dataId)) },
            { result -> _event.value = DilemmasEvent.GetComments(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

//    fun saveDataToUser(dataId: String) {
//        executeUseCase(
//            { saveDataUseCase.execute(SaveDataUseCase.Params(dataId)) },
//            { result -> _event.value = ConditionalsEvent.DataSaved(result) },
//            { _event.value = ConditionalsEvent.SWW }
//        )
//    }
//
//    fun checkIfAlreadyVoted(dataId: String) {
//        executeUseCase(
//            { checkIfAlreadySavedUseCase.execute(CheckIfAlreadySavedUseCase.Params(dataId)) },
//            { result -> _event.value = ConditionalsEvent.AlreadySaved(result) },
//            { _event.value = ConditionalsEvent.SWW }
//        )
//    }
//
//    fun checkIfAlreadySaved(dataId: String) {
//        executeUseCase(
//            { checkIfAlreadySavedUseCase.execute(CheckIfAlreadySavedUseCase.Params(dataId)) },
//            { result -> _event.value = ConditionalsEvent.AlreadySaved(result) },
//            { _event.value = ConditionalsEvent.SWW }
//        )
//    }
}