package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmas
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmasViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDilemmas: GetDilemmas,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val voteDilemmaCommentUseCase: VoteDilemmaCommentUseCase,
    private val setFavDilemmaUseCase: SetFavDilemmaUseCase
) : BaseViewModel<DilemmasEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.InitiatedSession(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getDilemmas() {
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

    fun favDilemma(dilemmaId: String, txtTop: String, txtBottom: String) {

        executeUseCase(
            {
                setFavDilemmaUseCase.execute(
                    SetFavDilemmaUseCase.Params(dilemmaId, txtTop, txtBottom)
                )
            },
            { },
            { _event.value = DilemmasEvent.SWW }
        )
    }
}