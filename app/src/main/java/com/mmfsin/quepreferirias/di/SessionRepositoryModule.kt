package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.SessionRepository
import com.mmfsin.quepreferirias.domain.interfaces.ISessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface SessionRepositoryModule {
    @Binds
    fun bind(repository: SessionRepository): ISessionRepository
}