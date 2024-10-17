package com.mmfsin.quepreferirias.presentation.ideas.otherideas.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetOtherUserDualismsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherDualismsViewModel @Inject constructor(
    private val getOtherUserDualismsUseCase: GetOtherUserDualismsUseCase,
) : BaseViewModel<OtherDualismsEvent>() {

    fun getUserDualisms(userId: String) {
        executeUseCase(
            { getOtherUserDualismsUseCase.execute(GetOtherUserDualismsUseCase.Params(userId)) },
            { result -> _event.value = OtherDualismsEvent.Dualisms(result) },
            { _event.value = OtherDualismsEvent.SWW }
        )
    }
}