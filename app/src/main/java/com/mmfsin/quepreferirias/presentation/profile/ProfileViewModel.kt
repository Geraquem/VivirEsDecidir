package com.mmfsin.quepreferirias.presentation.profile

import com.mmfsin.quepreferirias.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : BaseViewModel<ProfileEvent>() {

//    fun vote(dataId: String, isYes: Boolean) {
//        executeUseCase(
//            { userVoteUseCase.execute(UserVoteUseCase.Params(dataId, isYes)) },
//            { Log.i("userVoteUseCase: ", "User voted successfully") },
//            { _event.value = DashboardEvent.SWW }
//        )
//    }
}