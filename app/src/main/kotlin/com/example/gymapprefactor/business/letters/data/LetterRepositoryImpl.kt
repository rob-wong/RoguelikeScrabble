package com.example.gymapprefactor.business.letters.data

import com.example.gymapprefactor.business.letters.domain.LetterRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.Letter

class LetterRepositoryImpl(
    override val dataSource: LetterDataSource
) : LetterRepository {
    override suspend fun getLetters(): List<Letter> {
        return dataSource.fetchLetters()
    }
}

class LetterDataSource : DataSource {
    fun fetchLetters(): List<Letter> {
        return AppDataModel.letterList
    }
}
