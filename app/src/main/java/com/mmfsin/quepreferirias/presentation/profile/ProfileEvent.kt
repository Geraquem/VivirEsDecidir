package com.mmfsin.quepreferirias.presentation.profile

import com.mmfsin.quepreferirias.domain.models.Session

sealed class ProfileEvent {
    class Profile(val session: Session) : ProfileEvent()
    object SessionClosed : ProfileEvent()
    object UpdatedProfile : ProfileEvent()
    object SWW : ProfileEvent()
}