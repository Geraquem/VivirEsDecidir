package com.mmfsin.quepreferirias.presentation.single.dualism

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.usecases.CheckIfAlreadyVotedDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDataIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismByIdUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDualismUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleDualismViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val getDualismByIdUseCase: GetDualismByIdUseCase,
    private val getDualismVotesUseCase: GetDualismVotesUseCase,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val checkIfDataIsFavUseCase: CheckIfDataIsFavUseCase,
    private val setFavDataUseCase: SetFavDataUseCase,
    private val deleteFavDataUseCase: DeleteFavDataUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val voteDualismUseCase: VoteDualismUseCase,
    private val checkIfAlreadyVotedDataUseCase: CheckIfAlreadyVotedDataUseCase,
    private val reportDataUseCase: ReportDataUseCase,
) : BaseViewModel<SingleDualismEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDualismEvent.InitiatedSession(result) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = SingleDualismEvent.ReCheckSession(result) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun getSessionToComment() {
        executeUseCase(
            { getSessionUseCase.execute() },
            { result -> _event.value = SingleDualismEvent.GetSessionToComment(result) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun checkIfIsMe(userId: String) {
        executeUseCase(
            { checkIfUserIdIsMeUseCase.execute(CheckIfUserIdIsMeUseCase.Params(userId)) },
            { result -> _event.value = SingleDualismEvent.NavigateToProfile(result, userId) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun getSingleDualism(dualismId: String) {
        executeUseCase(
            { getDualismByIdUseCase.execute(GetDualismByIdUseCase.Params(dualismId)) },
            { result ->
                _event.value = result?.let { SingleDualismEvent.SingleDualism(it) }
                    ?: run { SingleDualismEvent.SWW }
            },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun getVotes(dualismId: String) {
        executeUseCase(
            { getDualismVotesUseCase.execute(GetDualismVotesUseCase.Params(dualismId)) },
            { result ->
                _event.value =
                    result?.let { SingleDualismEvent.GetVotes(it) }
                        ?: run { SingleDualismEvent.SWW }
            },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun checkIfVoted(dualismId: String) {
        executeUseCase(
            {
                checkIfAlreadyVotedDataUseCase.execute(
                    CheckIfAlreadyVotedDataUseCase.Params(dualismId, DUALISM)
                )
            },
            { result -> _event.value = SingleDualismEvent.AlreadyVoted(result) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun getPercents(votesTop: Long, votesBottom: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesTop, votesBottom)) },
            { result ->
                _event.value =
                    result?.let { SingleDualismEvent.GetPercents(it) }
                        ?: run { SingleDualismEvent.SWW }
            },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun checkIfIsFav(dualismId: String) {
        executeUseCase(
            { checkIfDataIsFavUseCase.execute(CheckIfDataIsFavUseCase.Params(dualismId, DUALISM)) },
            { result -> _event.value = SingleDualismEvent.CheckDualismFav(result) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun setDualismFav(dualismId: String, txtTop: String, txtBottom: String) {
        executeUseCase(
            {
                setFavDataUseCase.execute(
                    SetFavDataUseCase.Params(dualismId, DUALISM, txtTop, txtBottom)
                )
            },
            { Log.i("DUALISM_FAV", "DualismFav added") },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun voteDualism(dualismId: String, isTop: Boolean) {
        executeUseCase(
            { voteDualismUseCase.execute(VoteDualismUseCase.Params(dualismId, isTop)) },
            { _event.value = SingleDualismEvent.VoteDualism(wasTop = isTop) },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun deleteDualismFav(dualismId: String) {
        executeUseCase(
            { deleteFavDataUseCase.execute(DeleteFavDataUseCase.Params(dualismId, DUALISM)) },
            { Log.i("DUALISM_FAV", "DualismFav deleted") },
            { _event.value = SingleDualismEvent.SWW }
        )
    }

    fun reportDualism(dualism: Dualism) {
        executeUseCase(
            { reportDataUseCase.execute(ReportDataUseCase.Params(dualism, DUALISM)) },
            { _event.value = SingleDualismEvent.Reported },
            { _event.value = SingleDualismEvent.SWW }
        )
    }
}