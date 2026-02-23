package com.cypherose.business.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
interface User {
	val username: String
	val runesCount: Int
	val decks: List<Deck>
	val unlockedEffects: List<Effect>
	val previouslyPlayedEffects: List<String>
	val gameState: GameState
}

@Serializable
@SerialName("default")
class DefaultUser(
	override val username: String,
	override val runesCount: Int,
	override val decks: List<Deck>,
	override val unlockedEffects: List<Effect>,
	override val previouslyPlayedEffects: List<String> = emptyList(),
	override val gameState: GameState
): User

@Serializable
@SerialName("none")
object NoneUser : User {
	override val username = ""
	override val runesCount = 0
	override val decks = listOf<Deck>()
	override val unlockedEffects = listOf<Effect>()
	override val previouslyPlayedEffects = listOf<String>()
	override val gameState = NoneGameState
}

fun User.copy(
	username: String = this.username,
	runesCount: Int = this.runesCount,
	decks: List<Deck> = this.decks,
	unlockedEffects: List<Effect> = this.unlockedEffects,
	previouslyPlayedEffects: List<String> = this.previouslyPlayedEffects,
	gameState: GameState = this.gameState
): User {
	return when (this) {
		is DefaultUser -> DefaultUser(
			username = username,
			runesCount = runesCount,
			decks = decks,
			unlockedEffects = unlockedEffects,
			previouslyPlayedEffects = previouslyPlayedEffects,
			gameState = gameState
		)

		is NoneUser -> this
		else -> this
	}
}

// maybe eventually like PremiumUser or smt for paying users
