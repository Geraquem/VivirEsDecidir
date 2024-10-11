package com.mmfsin.quepreferirias.presentation.ideas.myideas.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteMyDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetMyDualismsUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDualismsViewModel @Inject constructor(
    private val getMyDualismsUseCase: GetMyDualismsUseCase,
    private val deleteMyDataUseCase: DeleteMyDataUseCase
) : BaseViewModel<MyDualismsEvent>() {

    fun getMyDualisms() {
        executeUseCase(
            { getMyDualismsUseCase.execute() },
            { result -> _event.value = MyDualismsEvent.Dualisms(result) },
            { _event.value = MyDualismsEvent.SWW }
        )
    }

    fun deleteMyDualism(dualismId: String) {
        executeUseCase(
            { deleteMyDataUseCase.execute(DeleteMyDataUseCase.Params(dualismId, DUALISM)) },
            { _event.value = MyDualismsEvent.Deleted },
            { _event.value = MyDualismsEvent.SWW }
        )
    }
}