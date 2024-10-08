package com.mmfsin.quepreferirias.presentation.dashboard.dilemmas

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDataIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteDilemmaFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmasUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmasViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val getDilemmasUseCase: GetDilemmasUseCase,
    private val getDilemmaVotesUseCase: GetDilemmaVotesUseCase,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val checkIfDataIsFavUseCase: CheckIfDataIsFavUseCase,
    private val setFavDilemmaUseCase: SetFavDilemmaUseCase,
    private val deleteDilemmaFavUseCase: DeleteDilemmaFavUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val voteDilemmaUseCase: VoteDilemmaUseCase,
    private val reportDataUseCase: ReportDataUseCase,
) : BaseViewModel<DilemmasEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.InitiatedSession(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getSessionToComment() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.GetSessionToComment(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DilemmasEvent.ReCheckSession(result) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun checkIfIsMe(userId: String) {
        executeUseCase(
            { checkIfUserIdIsMeUseCase.execute(CheckIfUserIdIsMeUseCase.Params(userId)) },
            { result -> _event.value = DilemmasEvent.NavigateToProfile(result, userId) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getDilemmas() {
        executeUseCase(
            { getDilemmasUseCase.execute() },
            { result ->
                _event.value =
                    if (result.isEmpty()) DilemmasEvent.SWW else DilemmasEvent.Dilemmas(result)
            },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getVotes(dilemmaId: String) {
        executeUseCase(
            { getDilemmaVotesUseCase.execute(GetDilemmaVotesUseCase.Params(dilemmaId)) },
            { result ->
                _event.value =
                    result?.let { DilemmasEvent.GetVotes(it) }
                        ?: run { DilemmasEvent.SWW }
            },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun getPercents(votesYes: Long, votesNo: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesYes, votesNo)) },
            { result ->
                _event.value = result?.let { DilemmasEvent.GetPercents(it) }
                    ?: run { DilemmasEvent.SWW }
            },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun checkIfIsFav(dilemmaId: String) {
        executeUseCase(
            { checkIfDataIsFavUseCase.execute(CheckIfDataIsFavUseCase.Params(dilemmaId, DILEMMA)) },
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

    fun voteDilemma(dilemmaId: String, isYes: Boolean) {
        executeUseCase(
            { voteDilemmaUseCase.execute(VoteDilemmaUseCase.Params(dilemmaId, isYes)) },
            { _event.value = DilemmasEvent.VoteDilemma(wasYes = isYes) },
            { _event.value = DilemmasEvent.SWW }
        )
    }

    fun reportDilemma(dilemma: Dilemma) {
        executeUseCase(
            { reportDataUseCase.execute(ReportDataUseCase.Params(dilemma, DILEMMA)) },
            { _event.value = DilemmasEvent.Reported },
            { _event.value = DilemmasEvent.SWW }
        )
    }
}