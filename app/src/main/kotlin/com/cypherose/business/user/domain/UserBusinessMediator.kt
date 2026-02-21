package com.cypherose.business.user.domain

import com.cypherose.business.errors.UserException
import com.cypherose.business.models.Deck
import com.cypherose.business.models.Letter
import com.cypherose.business.models.NoneUser
import com.cypherose.business.models.User
import com.cypherose.business.models.copy
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class UserBusinessMediator(
	private val getDecksUseCase: GetDecksUseCase,
	private val getUserUseCase: GetUserUseCase,
	private val saveUserUseCase: SaveUserUseCase,
	private val userRepository: UserRepository,
) {
	suspend fun getDecks(): Result<List<Deck>> {
		return getDecksUseCase().fold(
			onSuccess = { Result.success(it) },
			onFailure = { error ->
				Timber.e(error, "Failed to get decks")
				Result.failure(error)
			}
		)
	}

	suspend fun getUser(): Result<User> {
		return getUserUseCase().fold(
			onSuccess = { Result.success(it) },
			onFailure = { error ->
				Timber.e(error, "Failed to get user")
				Result.failure(error)
			}
		)
	}

	fun getUserFlow(): Flow<User?> {
		return userRepository.getUserFlow()
	}

	suspend fun upgradeLetter(deck: Deck, letter: Letter, cost: Int): Result<Unit> {
		val userResult = getUser()
		val validationError = validateUpgradeLetter(userResult, deck, letter, cost)
		if (validationError != null) {
			return Result.failure(validationError)
		}

		val user = userResult.getOrNull()!!
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

		return saveUserUseCase(updatedUser).fold(
			onSuccess = { Result.success(Unit) },
			onFailure = { error ->
				Timber.e(error, "Failed to save user after letter upgrade")
				Result.failure(error)
			}
		)
	}

	private fun validateUpgradeLetter(
		userResult: Result<User>,
		deck: Deck,
		letter: Letter,
		cost: Int
	): UserException? {
		val user = userResult.getOrNull() ?: return UserException.UserNotFound
		
		return when {
			user is NoneUser -> UserException.UserNotFound
			!deck.letters.contains(letter) -> UserException.LetterNotFound(letter.id)
			user.runesCount < cost -> UserException.InsufficientRunes(cost, user.runesCount)
			else -> null
		}
	}
}
