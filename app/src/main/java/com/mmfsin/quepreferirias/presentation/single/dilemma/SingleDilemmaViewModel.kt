package com.mmfsin.quepreferirias.presentation.single.dilemma

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDilemmaIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteDilemmaFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmas
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleDilemmaViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDilemmas: GetDilemmas,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase,
    private val checkIfDilemmaIsFavUseCase: CheckIfDilemmaIsFavUseCase,
    private val setFavDilemmaUseCase: SetFavDilemmaUseCase,
    private val deleteDilemmaFavUseCase: DeleteDilemmaFavUseCase
) : BaseViewModel<SingleDilemmaEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDilemmaEvent.InitiatedSession(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun reCheckSession(){
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDilemmaEvent.ReCheckSession(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getDilemmas() {
        executeUseCase(
            { getDilemmas.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) SingleDilemmaEvent.SWW else SingleDilemmaEvent.Dilemmas(result)
            },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getPercents(votesYes: Long, votesNo: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesYes, votesNo)) },
            { result ->
                _event.value =
                    result?.let { SingleDilemmaEvent.GetPercents(it) }
                        ?: run { SingleDilemmaEvent.SWW }
            },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getComments(dilemmaId: String? = null, fromRealm: Boolean) {
        executeUseCase(
            {
                getDilemmaCommentsUseCase.execute(
                    GetDilemmaCommentsUseCase.Params(dilemmaId = dilemmaId, fromRealm = fromRealm)
                )
            },
            { result -> _event.value = SingleDilemmaEvent.GetComments(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun checkIfIsFav(dilemmaId: String) {
        executeUseCase(
            { checkIfDilemmaIsFavUseCase.execute(CheckIfDilemmaIsFavUseCase.Params(dilemmaId)) },
            { result -> _event.value = SingleDilemmaEvent.CheckDilemmaFav(result) },
            { _event.value = SingleDilemmaEvent.SWW }
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
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun deleteDilemmaFav(dilemmaId: String) {
        executeUseCase(
            { deleteDilemmaFavUseCase.execute(DeleteDilemmaFavUseCase.Params(dilemmaId)) },
            { Log.i("DILEMMA_FAV", "DilemmaFav deleted") },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }
}