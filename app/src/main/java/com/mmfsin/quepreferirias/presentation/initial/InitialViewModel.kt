package com.mmfsin.quepreferirias.presentation.initial

import com.mmfsin.quepreferirias.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
) : BaseViewModel<InitialEvent>() {
}