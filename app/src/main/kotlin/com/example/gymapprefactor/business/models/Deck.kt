package com.example.gymapprefactor.business.models

// I don't love this name, I would do "set" but that's already a term
interface Deck {
	val id: String
	val letters: List<Letter>
	val name: String
}

class DefaultDeck(
	override val id: String,
	override val letters: List<Letter>,
	override val name: String
) : Deck
