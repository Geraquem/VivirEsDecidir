package com.mmfsin.quepreferirias.presentation.saved

import com.mmfsin.quepreferirias.base.BaseViewModel
import com.mmfsin.quepreferirias.domain.usecases.GetSavedDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDataViewModel @Inject constructor(
    private val getSavedDataUseCase: GetSavedDataUseCase,
) : BaseViewModel<SavedDataEvent>() {

    fun getSavedData() {
        executeUseCase(
            { getSavedDataUseCase.execute() },
            { result ->
                _event.value = result?.let { SavedDataEvent.Data(result) }
                    ?: run { SavedDataEvent.SWW }
            },
            { _event.value = SavedDataEvent.SWW })
    }
}