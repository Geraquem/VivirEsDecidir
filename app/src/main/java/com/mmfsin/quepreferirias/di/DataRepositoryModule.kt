package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.DataRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DataRepositoryModule {
    @Binds
    fun bind(repository: DataRepository): IDataRepository
}