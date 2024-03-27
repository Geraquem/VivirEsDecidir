package com.mmfsin.quepreferirias.presentation.profile

import com.mmfsin.quepreferirias.domain.models.SendDilemma
import com.mmfsin.quepreferirias.domain.models.Session

sealed class ProfileEvent {
    class Profile(val session: Session) : ProfileEvent()
    class MyDilemmas(val dilemmas: List<SendDilemma>) : ProfileEvent()
    object UpdatedProfile : ProfileEvent()
    object SessionClosed : ProfileEvent()
    object SWW : ProfileEvent()
}