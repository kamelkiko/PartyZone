package com.app.partyzone.core.di

import com.app.partyzone.core.data.repository.preferences.UserPreferencesRepository
import com.app.partyzone.core.data.repository.remote.AuthRepositoryImpl
import com.app.partyzone.core.data.repository.remote.PostRepositoryImpl
import com.app.partyzone.core.data.repository.remote.SellerRepositoryImpl
import com.app.partyzone.core.data.repository.remote.UserRepositoryImpl
import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.core.domain.repository.IUserPreferencesRepository
import com.app.partyzone.core.domain.repository.PostRepository
import com.app.partyzone.core.domain.repository.SellerRepository
import com.app.partyzone.core.domain.repository.UserRepository
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
    abstract fun bindUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindSellerRepository(
        sellerRepository: SellerRepositoryImpl
    ): SellerRepository

    @Singleton
    @Binds
    abstract fun bindPostRepository(
        postRepository: PostRepositoryImpl
    ): PostRepository
}