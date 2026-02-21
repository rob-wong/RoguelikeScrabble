package com.cypherose.business.letters.data

import com.cypherose.business.letters.domain.LetterRepository
import com.cypherose.business.interfaces.DataSource
import com.cypherose.business.models.Letter

class LetterRepositoryImpl(
    override val dataSource: LetterDataSource
) : LetterRepository {
    override suspend fun getLetters(): List<Letter> {
        return dataSource.fetchLetters()
    }
}

class LetterDataSource : DataSource {
    fun fetchLetters(): List<Letter> {
        return listOf()
    }
}
