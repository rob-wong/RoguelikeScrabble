package com.cypherose.business.letters.domain

import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.Letter

class GetLettersUseCase(override val repository: LetterRepository) : UseCase {
    suspend operator fun invoke(): List<Letter> {
        return repository.getLetters()
    }
}
