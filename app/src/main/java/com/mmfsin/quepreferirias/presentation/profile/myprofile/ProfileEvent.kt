package com.mmfsin.quepreferirias.presentation.profile.myprofile

import com.mmfsin.quepreferirias.domain.models.Session

sealed class ProfileEvent {
    class Profile(val session: Session) : ProfileEvent()
    object UpdatedProfile : ProfileEvent()
    object SessionClosed : ProfileEvent()
    object SWW : ProfileEvent()
}