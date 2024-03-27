package com.mmfsin.quepreferirias.presentation.profile.otherprofile

import com.mmfsin.quepreferirias.domain.models.Session

sealed class OtherProfileEvent {
    class Profile(val session: Session) : OtherProfileEvent()
    object SWW : OtherProfileEvent()
}