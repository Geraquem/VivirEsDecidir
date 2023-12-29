package com.mmfsin.quepreferirias.di

import com.mmfsin.quepreferirias.data.repository.UserRepository
import com.mmfsin.quepreferirias.domain.interfaces.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UserRepositoryModule {
    @Binds
    fun bind(repository: UserRepository): IUserRepository
}