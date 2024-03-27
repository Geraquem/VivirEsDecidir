package com.mmfsin.quepreferirias.presentation.profile.listeners

import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.RRSSType

interface IRRSSListener {
    fun updateRRSS(rrss: RRSS)
    fun onRRSSClick(type: RRSSType, name: String)
}