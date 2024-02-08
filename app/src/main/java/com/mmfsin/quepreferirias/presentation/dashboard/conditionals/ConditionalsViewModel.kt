package com.mmfsin.quepreferirias.presentation.dashboard.conditionals

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConditionalsViewModel @Inject constructor(
    private val getConditionalData: GetConditionalData,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val userVoteUseCase: UserVoteUseCase,
    private val checkIfAlreadySavedUseCase: CheckIfAlreadySavedUseCase,
    private val saveDataUseCase: SaveDataUseCase
) : BaseViewModel<ConditionalsEvent>() {

    fun getConditionalData() {
        executeUseCase(
            { getConditionalData.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) ConditionalsEvent.SWW else ConditionalsEvent.Data(result)
            },
            { _event.value = ConditionalsEvent.SWW }
        )
    }

    fun getPercents(votesYes: Long, votesNo: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesYes, votesNo)) },
            { result ->
                _event.value =
                    result?.let { ConditionalsEvent.GetPercents(it) } ?: run { ConditionalsEvent.SWW }
            },
            { _event.value = ConditionalsEvent.SWW }
        )
    }

    fun vote(dataId: String, isYes: Boolean) {
        executeUseCase(
            { userVoteUseCase.execute(UserVoteUseCase.Params(dataId, isYes)) },
            { Log.i("userVoteUseCase: ", "User voted successfully") },
            { _event.value = ConditionalsEvent.SWW }
        )
    }

    fun saveDataToUser(dataId: String) {
        executeUseCase(
            { saveDataUseCase.execute(SaveDataUseCase.Params(dataId)) },
            { result -> _event.value = ConditionalsEvent.DataSaved(result) },
            { _event.value = ConditionalsEvent.SWW }
        )
    }

    fun checkIfAlreadyVoted(dataId: String) {
        executeUseCase(
            { checkIfAlreadySavedUseCase.execute(CheckIfAlreadySavedUseCase.Params(dataId)) },
            { result -> _event.value = ConditionalsEvent.AlreadySaved(result) },
            { _event.value = ConditionalsEvent.SWW }
        )
    }

    fun checkIfAlreadySaved(dataId: String) {
        executeUseCase(
            { checkIfAlreadySavedUseCase.execute(CheckIfAlreadySavedUseCase.Params(dataId)) },
            { result -> _event.value = ConditionalsEvent.AlreadySaved(result) },
            { _event.value = ConditionalsEvent.SWW }
        )
    }
}