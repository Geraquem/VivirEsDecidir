package com.mmfsin.quepreferirias.presentation.single.dilemma

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyVotedDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDilemmaIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteDilemmaFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaByIdUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaCommentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDilemmaVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDilemmaUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDilemmaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleDilemmaViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDilemmaByIdUseCase: GetDilemmaByIdUseCase,
    private val getDilemmaVotesUseCase: GetDilemmaVotesUseCase,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val getDilemmaCommentsUseCase: GetDilemmaCommentsUseCase,
    private val checkIfDilemmaIsFavUseCase: CheckIfDilemmaIsFavUseCase,
    private val setFavDilemmaUseCase: SetFavDilemmaUseCase,
    private val deleteDilemmaFavUseCase: DeleteDilemmaFavUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val voteDilemmaUseCase: VoteDilemmaUseCase,
    private val checkIfAlreadyVotedDilemmaUseCase: CheckIfAlreadyVotedDilemmaUseCase
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
                checkIfAlreadyVotedDilemmaUseCase.execute(
                    CheckIfAlreadyVotedDilemmaUseCase.Params(dilemmaId)
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

    fun voteDilemma(dilemmaId: String, isYes: Boolean) {
        executeUseCase(
            { voteDilemmaUseCase.execute(VoteDilemmaUseCase.Params(dilemmaId, isYes)) },
            { _event.value = SingleDilemmaEvent.VoteDilemma(wasYes = isYes) },
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