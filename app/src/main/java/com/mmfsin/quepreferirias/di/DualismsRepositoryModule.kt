package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.DualismsRepository
import com.mmfsin.quepreferirias.domain.interfaces.IDualismsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DualismsRepositoryModule {
    @Binds
    fun bind(repository: DualismsRepository): IDualismsRepository
}