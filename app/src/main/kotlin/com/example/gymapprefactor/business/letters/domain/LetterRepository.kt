package com.example.gymapprefactor.business.letters.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.Letter

interface LetterRepository : Repository {
    suspend fun getLetters(): List<Letter>
}
