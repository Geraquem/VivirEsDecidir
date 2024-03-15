package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.DilemmasRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDilemmasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DilemmasRepositoryModule {
    @Binds
    fun bind(repository: DilemmasRepository): IDilemmasRepository
}