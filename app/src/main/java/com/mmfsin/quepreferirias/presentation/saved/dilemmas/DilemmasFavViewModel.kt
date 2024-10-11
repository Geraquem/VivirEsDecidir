package com.mmfsin.quepreferirias.presentation.saved.dilemmas

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.DeleteFavDataUseCase
import com.mmfsin.quepreferirias.domain.usecases.GetFavDilemmasUseCase
import com.mmfsin.quepreferirias.presentation.models.DashboardType.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DilemmasFavViewModel @Inject constructor(
    private val getFavDilemmasUseCase: GetFavDilemmasUseCase,
    private val deleteFavDataUseCase: DeleteFavDataUseCase
) : BaseViewModel<DilemmasFavEvent>() {

    fun getFavDilemmas() {
        executeUseCase(
            { getFavDilemmasUseCase.execute() },
            { result -> _event.value = DilemmasFavEvent.Data(result) },
            { _event.value = DilemmasFavEvent.SWW }
        )
    }

    fun deleteDilemmaFav(dilemmaId: String) {
        executeUseCase(
            { deleteFavDataUseCase.execute(DeleteFavDataUseCase.Params(dilemmaId, DILEMMA)) },
            { _event.value = DilemmasFavEvent.FavDeleted },
            { _event.value = DilemmasFavEvent.SWW }
        )
    }
}