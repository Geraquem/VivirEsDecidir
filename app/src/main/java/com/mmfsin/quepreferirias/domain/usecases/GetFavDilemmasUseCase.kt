package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.DilemmaFav
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetFavDilemmasUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: IUserRepository,
    private val dataRepository: IDilemmasRepository
) : BaseUseCaseNoParams<List<DilemmaFav>?>() {

    override suspend fun execute(): List<DilemmaFav>? {
        return emptyList()
    }
}