package com.example.gymapprefactor.business.user.domain

import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.NoneUser
import com.example.gymapprefactor.business.models.User
import com.example.gymapprefactor.business.models.copy

class UserBusinessMediator(
	private val getDecksUseCase: GetDecksUseCase,
	private val getUserUseCase: GetUserUseCase,
	private val saveUserUseCase: SaveUserUseCase,
) {
	suspend fun getDecks(): List<Deck> {
		return getDecksUseCase().fold(
			onSuccess = { it },
			onFailure = { emptyList() }
		)
	}

	suspend fun getUser(): User {
		return getUserUseCase().fold(
			onSuccess = { it },
			onFailure = { NoneUser } // error dialog
		)
	}

	suspend fun upgradeLetter(deck: Deck, letter: Letter) {
		val user = getUser().also { if (it is NoneUser) return } // exit early if not logged in

		val cost = 0 // figure out source of truth for cost, prolly a mapper

		if (!deck.letters.contains(letter) || user.runesCount < cost) {
			return // error dialog
		}

		val upgradedDeck = deck.copy(
			letters = deck.letters.map {
				if (it.id == letter.id) letter.copy(level = letter.level + 1) else it
			}
		)

		val updatedUser = user.copy(
			runesCount = user.runesCount - cost,
			decks = user.decks.map {
				if (it.id == deck.id) upgradedDeck else it
			}
		)

		// Save the updated user using your use case or repository
		saveUserUseCase(updatedUser)
	}
}
