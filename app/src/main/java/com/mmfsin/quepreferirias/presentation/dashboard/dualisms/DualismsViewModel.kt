package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import android.util.Log
import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.models.Dualism
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDataIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.DeleteFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismsUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetPercentsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.domain.usecases.ReportDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.SetFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.VoteDualismUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DualismsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDualismsUseCase: GetDualismsUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val checkIfDataIsFavUseCase: CheckIfDataIsFavUseCase,
    private val getDualismVotesUseCase: GetDualismVotesUseCase,
    private val voteDualismUseCase: VoteDualismUseCase,
    private val getPercentsUseCase: GetPercentsUseCase,
    private val setFavDataUseCase: SetFavDataUseCase,
    private val deleteFavDataUseCase: DeleteFavDataUseCase,
    private val reportDataUseCase: ReportDataUseCase
) : BaseViewModel<DualismsEvent>() {

    fun checkSessionInitiated() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DualismsEvent.InitiatedSession(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun reCheckSession() {
        executeUseCase(
            { initiatedSessionUseCase.execute() },
            { result -> _event.value = DualismsEvent.ReCheckSession(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun checkIfIsMe(userId: String) {
        executeUseCase(
            { checkIfUserIdIsMeUseCase.execute(CheckIfUserIdIsMeUseCase.Params(userId)) },
            { result -> _event.value = DualismsEvent.NavigateToProfile(result, userId) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun getDualisms() {
        executeUseCase(
            { getDualismsUseCase.execute() },
            { result -> _event.value = DualismsEvent.Dualisms(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun checkIfIsFav(dualismId: String) {
        executeUseCase(
            { checkIfDataIsFavUseCase.execute(CheckIfDataIsFavUseCase.Params(dualismId, DUALISM)) },
            { result -> _event.value = DualismsEvent.CheckDualismFav(result) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun getVotes(dilemmaId: String) {
        executeUseCase(
            { getDualismVotesUseCase.execute(GetDualismVotesUseCase.Params(dilemmaId)) },
            { result ->
                _event.value =
                    result?.let { DualismsEvent.GetVotes(it) }
                        ?: run { DualismsEvent.SWW }
            },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun voteDualism(dualismId: String, isTop: Boolean) {
        executeUseCase(
            { voteDualismUseCase.execute(VoteDualismUseCase.Params(dualismId, isTop)) },
            { _event.value = DualismsEvent.VoteDilemma(wasTop = isTop) },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun getPercents(votesTop: Long, votesBottom: Long) {
        executeUseCase(
            { getPercentsUseCase.execute(GetPercentsUseCase.Params(votesTop, votesBottom)) },
            { result ->
                _event.value = result?.let { DualismsEvent.GetPercents(it) }
                    ?: run { DualismsEvent.SWW }
            },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun setDualismFav(dualismId: String, txtTop: String, txtBottom: String) {
        executeUseCase(
            {
                setFavDataUseCase.execute(
                    SetFavDataUseCase.Params(dualismId, DUALISM, txtTop, txtBottom)
                )
            },
            { Log.i("DILEMMA_FAV", "DilemmaFav added") },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun deleteFavDualism(dualismId: String) {
        executeUseCase(
            { deleteFavDataUseCase.execute(DeleteFavDataUseCase.Params(dualismId, DUALISM)) },
            { Log.i("DILEMMA_FAV", "DilemmaFav deleted") },
            { _event.value = DualismsEvent.SWW }
        )
    }

    fun reportDualism(dualism: Dualism) {
        executeUseCase(
            { reportDataUseCase.execute(ReportDataUseCase.Params(dualism, DUALISM)) },
            { _event.value = DualismsEvent.Reported },
            { _event.value = DualismsEvent.SWW }
        )
    }
}