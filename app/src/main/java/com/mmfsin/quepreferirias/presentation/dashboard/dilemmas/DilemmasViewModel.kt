package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDilemmaIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteDilemmaFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmas
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmasViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDilemmas: GetDilemmas,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val checkIfDilemmaIsFavUseCase: CheckIfDilemmaIsFavUseCase,
    private val setFavDilemmaUseCase: SetFavDilemmaUseCase,
    private val deleteDilemmaFavUseCase: DeleteDilemmaFavUseCase
) : BaseViewModel<DilemmasEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.InitiatedSession(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun reCheckSession(){
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.ReCheckSession(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getDilemmas() {
        executeUseCase(
            { getDilemmas.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) DilemmasEvent.SWW else DilemmasEvent.Dilemmas(result)
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

    fun getComments(dilemmaId: String? = null, fromRealm: Boolean) {
        executeUseCase(
            {
                getDilemmaCommentsUseCase.execute(
                    GetDilemmaCommentsUseCase.Params(dilemmaId = dilemmaId, fromRealm = fromRealm)
                )
            },
            { result -> _event.value = DilemmasEvent.GetComments(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun checkIfIsFav(dilemmaId: String) {
        executeUseCase(
            { checkIfDilemmaIsFavUseCase.execute(CheckIfDilemmaIsFavUseCase.Params(dilemmaId)) },
            { result -> _event.value = DilemmasEvent.CheckDilemmaFav(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun setDilemmaFav(dilemmaId: String, txtTop: String, txtBottom: String) {
        executeUseCase(
            {
                setFavDilemmaUseCase.execute(
                    SetFavDilemmaUseCase.Params(dilemmaId, txtTop, txtBottom)
                )
            },
            { Log.i("DILEMMA_FAV", "DilemmaFav added") },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun deleteDilemmaFav(dilemmaId: String) {
        executeUseCase(
            { deleteDilemmaFavUseCase.execute(DeleteDilemmaFavUseCase.Params(dilemmaId)) },
            { Log.i("DILEMMA_FAV", "DilemmaFav deleted") },
            { _event.value = DilemmasEvent.SWW }
        )
    }
}