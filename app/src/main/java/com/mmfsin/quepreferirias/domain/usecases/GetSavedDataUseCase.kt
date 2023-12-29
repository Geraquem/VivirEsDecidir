package com.mmfsin.quepreferirias.domain.usecases

import android.content.Context
import com.mmfsin.quepreferirias.base.BaseUseCaseNoParams
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import com.mmfsin.quepreferirias.domain.models.SavedData
import com.mmfsin.quepreferirias.utils.SESSION
import com.mmfsin.quepreferirias.utils.UPDATE_SAVED_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetSavedDataUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val sessionRepository: IUserRepository,
    private val dataRepository: IDataRepository
) : BaseUseCaseNoParams<List<SavedData>?>() {

    override suspend fun execute(): List<SavedData>? {
        val session = sessionRepository.getSession() ?: return null

        var savedData = dataRepository.getSavedDataInRealm()

        val sharedPrefs = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val update = sharedPrefs.getBoolean(UPDATE_SAVED_DATA, true)
        if (update) savedData = getSavedDataFromFirebase(session.email)

        return savedData
    }

    private suspend fun getSavedDataFromFirebase(email: String): List<SavedData> {
        val keys = sessionRepository.getSavedDataKeys(email)
        return dataRepository.getDataGivenKeyList(keys)
    }
}