package com.example.gymapprefactor.business.models

// I don't love this name, I would do "set" but that's already a term
interface Deck {
	val id: String
	val letters: List<Letter>
	val name: String

	fun size() = letters.size
}

class DefaultDeck(
	override val id: String,
	override val letters: List<Letter>,
	override val name: String
) : Deck

fun Deck.copy(
	id: String = this.id,
	letters: List<Letter> = this.letters,
	name: String = this.name
): Deck {
	return when (this) {
		is DefaultDeck -> DefaultDeck(
			id = id,
			letters = letters,
			name = name
		)

		else -> this
	}
}
