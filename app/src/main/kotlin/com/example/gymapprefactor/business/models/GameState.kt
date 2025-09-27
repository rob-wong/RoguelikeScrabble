package com.example.gymapprefactor.business.models

interface GameState

class NoneGameState : GameState

class ActiveGameState(
	val activeGameVariables: ActiveGameVariables,
	val activeGameValues: ActiveGameValues,
	val currentRound: CurrentRound,
) : GameState

class ActiveGameVariables(
	var glyphCount: Int,
	var runesCount: Int,
	var stage: Int, // could also have a stage model w/a level model w/a round model
	var level: Int,
	var maxRounds: Int,
)

class ActiveGameValues(
	val seed: Int,
	val deck: Deck,
	val effects: MutableList<Effect>,
)

class CurrentRound(
	val round: Int,
	val enemyHealth: Int,
	val wordsPlayed: List<String>,
)
