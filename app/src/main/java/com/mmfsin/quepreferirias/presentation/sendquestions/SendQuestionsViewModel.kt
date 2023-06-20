package com.mmfsin.quepreferirias.presentation.sendquestions

import com.mmfsin.quepreferirias.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendQuestionsViewModel @Inject constructor(
) : BaseViewModel<SendQuestionsEvent>() {


    fun vote(dataId: String, isYes: Boolean) {
//        executeUseCase(
//            { userVoteUseCase.execute(UserVoteUseCase.Params(dataId, isYes)) },
//            { Log.i("userVoteUseCase: ", "User voted successfully") },
//            { _event.value = DashboardEvent.SWW }
//        )
    }
}