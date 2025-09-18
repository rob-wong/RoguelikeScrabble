package com.example.gymapprefactor.business.letters.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.Letter

class GetLettersUseCase(override val repository: LetterRepository) : UseCase {
    suspend operator fun invoke(): List<Letter> {
        return repository.getLetters()
    }
}
