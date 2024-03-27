package com.mmfsin.quepreferirias.presentation.profile.common.listeners

import com.mmfsin.quepreferirias.domain.models.RRSS
import com.mmfsin.quepreferirias.domain.models.RRSSType

interface IRRSSListener {
    fun updateRRSS(rrss: RRSS)
    fun onRRSSClick(type: RRSSType, name: String)
}