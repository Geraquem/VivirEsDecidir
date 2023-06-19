package com.mmfsin.quepreferirias.domain.usecases

import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.models.Data
import javax.inject.Inject

class GetAppDataUseCase @Inject constructor(private val repository: IDataRepository) :
    BaseUseCaseNoParams<List<Data>>() {

    override suspend fun execute(): List<Data> = repository.getDataFromFirebase()
}