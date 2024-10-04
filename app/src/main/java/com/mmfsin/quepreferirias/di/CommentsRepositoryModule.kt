package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.CommentsRepository
import com.mmfsin.quepreferirias.domain.interfaces.ICommentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CommentsRepositoryModule {
    @Binds
    fun bind(repository: CommentsRepository): ICommentsRepository
}