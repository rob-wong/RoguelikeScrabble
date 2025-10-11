package com.example.gymapprefactor.business.user.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.Deck

class GetDecksUseCase(override val repository: UserRepository) : UseCase {
	suspend operator fun invoke(): Result<List<Deck>> {
		val user = repository.getUser()

		return if (user != null) {
			Result.success(user.decks)
		} else {
			Result.failure(IllegalStateException("User not found in repository"))
		}
	}
}
