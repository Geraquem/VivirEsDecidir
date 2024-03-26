package com.mmfsin.quepreferirias.presentation.profile.listeners

import com.mmfsin.quepreferirias.domain.models.RRSS

interface IRRSSListener {
    fun updateRRSS(rrss: RRSS)
}