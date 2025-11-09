package com.example.gymapprefactor.business.models

interface GameState

class NoneGameState : GameState

data class ActiveGameState(
	val activeGameVariables: ActiveGameVariables,
	val activeGameValues: ActiveGameValues,
	val currentRound: CurrentRound,
) : GameState

data class ActiveGameVariables(
	val glyphCount: Int,
	val runesCount: Int,
	val stage: Int, // could also have a stage model w/a level model w/a round model
	val level: Int,
	val maxRounds: Int,
	val maxDiscards: Int,
	val handSize: Int,
	val gameLost: Boolean,
)

data class ActiveGameValues(
	val seed: Long,
	val deck: Deck,
	val effects: List<Effect>,
)

data class CurrentRound(
	val round: Int,
	val discardsUsed : Int,
	val enemyHealth: Int,
	val wordsPlayed: List<String>,
	val mutableDeck: Deck,
	val hand: List<Letter>
)
