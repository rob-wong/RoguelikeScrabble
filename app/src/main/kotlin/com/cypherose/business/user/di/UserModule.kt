package com.cypherose.business.user.di

import com.cypherose.business.models.AppDataModel
import com.cypherose.business.user.data.UserDataSource
import com.cypherose.business.user.data.UserRepositoryImpl
import com.cypherose.business.user.domain.GetDecksUseCase
import com.cypherose.business.user.domain.GetUserUseCase
import com.cypherose.business.user.domain.SaveUserUseCase
import com.cypherose.business.user.domain.UserBusinessMediator
import com.cypherose.business.user.domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun provideUserDataSource(
        appDataModel: AppDataModel
    ): UserDataSource {
        return UserDataSource(appDataModel)
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
        userRepository: UserRepository,
    ): UserBusinessMediator {
        return UserBusinessMediator(
            getUserUseCase = getUserUseCase,
            getDecksUseCase = getDecksUseCase,
            saveUserUseCase = saveUserUseCase,
            userRepository = userRepository,
        )
    }
}
