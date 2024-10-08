package com.mmfsin.quepreferirias.presentation.dashboard.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.CheckIfDataIsFavUseCase
import com.mmfsin.quepreferirias.domain.usecases.CheckIfUserIdIsMeUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismVotesUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetDualismsUseCase
import com.mmfsin.quepreferirias.domain.usecases.InitiatedSessionUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DualismsViewModel @Inject constructor(
    private val initiatedSessionUseCase: InitiatedSessionUseCase,
    private val getDualismsUseCase: GetDualismsUseCase,
    private val checkIfUserIdIsMeUseCase: CheckIfUserIdIsMeUseCase,
    private val checkIfDataIsFavUseCase: CheckIfDataIsFavUseCase,
    private val getDualismVotesUseCase: GetDualismVotesUseCase
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
}