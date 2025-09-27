package com.example.gymapprefactor.business.models

interface User {
	val username: String
	val runesCount: Int
	val decks: List<Deck>
	val unlockedEffects : List<Effect>
	val gameState: GameState
}

class DefaultUser(
	override val username: String,
	override val runesCount: Int,
	override val decks: List<Deck>,
	override val unlockedEffects: List<Effect>,
	override val gameState: GameState
): User

// maybe eventually like PremiumUser or smt for paying users
