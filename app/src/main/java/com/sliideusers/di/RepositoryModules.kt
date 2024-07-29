package com.sliideusers.di

import com.sliideusers.data.api.user.UserApiService
import com.sliideusers.data.repositories.user.UserRepositoryImpl
import com.sliideusers.domain.repositories.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModules {
    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        coroutineDispatcher: CoroutineDispatcher,
    ): UserRepository {
        return UserRepositoryImpl(
            userApiService,
            coroutineDispatcher
        )
    }
}