package com.example.gymapprefactor.business.user.di

import com.example.gymapprefactor.business.network.UserStorage
import com.example.gymapprefactor.business.user.data.UserDataSource
import com.example.gymapprefactor.business.user.data.UserRepositoryImpl
import com.example.gymapprefactor.business.user.domain.GetDecksUseCase
import com.example.gymapprefactor.business.user.domain.GetUserUseCase
import com.example.gymapprefactor.business.user.domain.SaveUserUseCase
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import com.example.gymapprefactor.business.user.domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun provideUserDataSource(
        userStorage: UserStorage
    ): UserDataSource {
        return UserDataSource(userStorage)
    }

    @Provides
    fun provideUserRepository(
        dataSource: UserDataSource
    ): UserRepository {
        return UserRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetUsersUseCase(
        repository: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Provides
    fun provideSaveUsersUseCase(
        repository: UserRepository
    ): SaveUserUseCase {
        return SaveUserUseCase(repository)
    }

    @Provides
    fun provideGetDecksUseCase(
        repository: UserRepository
    ): GetDecksUseCase {
        return GetDecksUseCase(repository)
    }

    @Provides
    fun provideUserBusinessMediator(
        getDecksUseCase: GetDecksUseCase,
        getUserUseCase: GetUserUseCase,
        saveUserUseCase: SaveUserUseCase,
    ): UserBusinessMediator {
        return UserBusinessMediator(
            getUserUseCase = getUserUseCase,
            getDecksUseCase = getDecksUseCase,
            saveUserUseCase = saveUserUseCase,
        )
    }
}
