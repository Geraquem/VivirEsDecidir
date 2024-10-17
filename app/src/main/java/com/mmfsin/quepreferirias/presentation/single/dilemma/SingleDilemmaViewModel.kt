package com.mmfsin.quepreferirias.presentation.single.dilemma

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Dilemma
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyVotedDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDataIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaByIdUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DILEMMA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleDilemmaViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val getDilemmaByIdUseCase: GetDilemmaByIdUseCase,
    private val getDilemmaVotesUseCase: GetDilemmaVotesUseCase,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val checkIfDataIsFavUseCase: CheckIfDataIsFavUseCase,
    private val setFavDataUseCase: SetFavDataUseCase,
    private val deleteFavDataUseCase: DeleteFavDataUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val voteDilemmaUseCase: VoteDilemmaUseCase,
    private val checkIfAlreadyVotedDataUseCase: CheckIfAlreadyVotedDataUseCase,
    private val reportDataUseCase: ReportDataUseCase,
) : BaseViewModel<SingleDilemmaEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDilemmaEvent.InitiatedSession(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDilemmaEvent.ReCheckSession(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getSessionToComment() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = SingleDilemmaEvent.GetSessionToComment(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun checkIfIsMe(userId: String) {
        executeUseCase(
            { checkIfUserIdIsMeUseCase.execute(CheckIfUserIdIsMeUseCase.Params(userId)) },
            { result -> _event.value = SingleDilemmaEvent.NavigateToProfile(result, userId) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getSingleDilemma(dilemmaId: String) {
        executeUseCase(
            { getDilemmaByIdUseCase.execute(GetDilemmaByIdUseCase.Params(dilemmaId)) },
            { result ->
                _event.value = result?.let { SingleDilemmaEvent.SingleDilemma(it) }
                    ?: run { SingleDilemmaEvent.SWW }
            },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun getVotes(dilemmaId: String) {
        executeUseCase(
            { getDilemmaVotesUseCase.execute(GetDilemmaVotesUseCase.Params(dilemmaId)) },
            { result ->
                _event.value =
                    result?.let { SingleDilemmaEvent.GetVotes(it) }
                        ?: run { SingleDilemmaEvent.SWW }
            },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun checkIfVoted(dilemmaId: String) {
        executeUseCase(
            {
                checkIfAlreadyVotedDataUseCase.execute(
                    CheckIfAlreadyVotedDataUseCase.Params(dilemmaId, DILEMMA)
                )
            },
            { result -> _event.value = SingleDilemmaEvent.AlreadyVoted(result) },
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

    fun checkIfIsFav(dilemmaId: String) {
        executeUseCase(
            { checkIfDataIsFavUseCase.execute(CheckIfDataIsFavUseCase.Params(dilemmaId, DILEMMA)) },
            { result -> _event.value = SingleDilemmaEvent.CheckDilemmaFav(result) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun setDilemmaFav(dilemmaId: String, txtTop: String, txtBottom: String) {
        executeUseCase(
            {
                setFavDataUseCase.execute(
                    SetFavDataUseCase.Params(dilemmaId, DILEMMA, txtTop, txtBottom)
                )
            },
            { Log.i("DILEMMA_FAV", "DilemmaFav added") },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun voteDilemma(dilemmaId: String, isYes: Boolean) {
        executeUseCase(
            { voteDilemmaUseCase.execute(VoteDilemmaUseCase.Params(dilemmaId, isYes)) },
            { _event.value = SingleDilemmaEvent.VoteDilemma(wasYes = isYes) },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun deleteDilemmaFav(dilemmaId: String) {
        executeUseCase(
            { deleteFavDataUseCase.execute(DeleteFavDataUseCase.Params(dilemmaId, DILEMMA)) },
            { Log.i("DILEMMA_FAV", "DilemmaFav deleted") },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }

    fun reportDilemma(dilemma: Dilemma) {
        executeUseCase(
            { reportDataUseCase.execute(ReportDataUseCase.Params(dilemma, DILEMMA)) },
            { _event.value = SingleDilemmaEvent.Reported },
            { _event.value = SingleDilemmaEvent.SWW }
        )
    }
}