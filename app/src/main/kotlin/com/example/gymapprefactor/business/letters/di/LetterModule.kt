package com.example.gymapprefactor.business.letters.di

import com.example.gymapprefactor.business.letters.data.LetterDataSource
import com.example.gymapprefactor.business.letters.data.LetterRepositoryImpl
import com.example.gymapprefactor.business.letters.domain.LetterRepository
import com.example.gymapprefactor.business.letters.domain.GetLettersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LetterModule {

    @Provides
    fun provideLetterDataSource(): LetterDataSource {
        return LetterDataSource()
    }

    @Provides
    fun provideLetterRepository(
        dataSource: LetterDataSource
    ): LetterRepository {
        return LetterRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetLettersUseCase(
        repository: LetterRepository
    ): GetLettersUseCase {
        return GetLettersUseCase(repository)
    }
}
