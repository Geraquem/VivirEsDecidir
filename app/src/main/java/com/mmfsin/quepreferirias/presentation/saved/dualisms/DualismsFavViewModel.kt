package com.mmfsin.quepreferirias.presentation.saved.dualisms

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetFavDualismsUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.DUALISM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DualismsFavViewModel @Inject constructor(
    private val getFavDualismsUseCase: GetFavDualismsUseCase,
    private val deleteFavDataUseCase: DeleteFavDataUseCase
) : BaseViewModel<DualismsFavEvent>() {

    fun getFavDualisms() {
        executeUseCase(
            { getFavDualismsUseCase.execute() },
            { result -> _event.value = DualismsFavEvent.Data(result) },
            { _event.value = DualismsFavEvent.SWW }
        )
    }

    fun deleteDualismFav(dilemmaId: String) {
        executeUseCase(
            { deleteFavDataUseCase.execute(DeleteFavDataUseCase.Params(dilemmaId, DUALISM)) },
            { _event.value = DualismsFavEvent.FavDeleted },
            { _event.value = DualismsFavEvent.SWW }
        )
    }
}