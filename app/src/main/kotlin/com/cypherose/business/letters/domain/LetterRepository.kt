package com.cypherose.business.letters.domain

import com.cypherose.business.interfaces.Repository
import com.cypherose.business.models.Letter

interface LetterRepository : Repository {
    suspend fun getLetters(): List<Letter>
}
