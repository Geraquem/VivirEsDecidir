package com.mmfsin.quepreferirias.domain.models

import com.mmfsin.quepreferirias.presentation.models.DashboardType

open class DataToRespondComment(
    val dataId: String,
    val type: DashboardType,
    val commentId: String,
    val commentText: String
)