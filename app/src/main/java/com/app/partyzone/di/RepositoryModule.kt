package com.app.partyzone.di

import com.app.partyzone.data.repository.preferences.UserPreferencesRepository
import com.app.partyzone.data.repository.remote.AuthGoogleRepoImpl
import com.app.partyzone.data.repository.remote.AuthRepositoryImpl
import com.app.partyzone.domain.repository.AuthGoogleRepository
import com.app.partyzone.domain.repository.AuthRepository
import com.app.partyzone.domain.repository.IUserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): IUserPreferencesRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

    @Singleton
    @Binds
    abstract fun bindAuthGoogleRepository(
        authRepository: AuthGoogleRepoImpl
    ): AuthGoogleRepository
}