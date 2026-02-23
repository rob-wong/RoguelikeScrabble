package com.cypherose.business.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Polymorphic
interface GameState

@Serializable
@SerialName("none")
object NoneGameState : GameState

@Serializable
@SerialName("active")
data class ActiveGameState(
	val activeGameVariables: ActiveGameVariables,
	val activeGameValues: ActiveGameValues,
	val currentRound: CurrentRound,
	/**
	 * Effect labels the user has previously played. Excluded from serialization via [Transient]
	 * because this list lives on [User.previouslyPlayedEffects] and is merged in at runtime when
	 * fetching game state. Keeping it transient avoids duplicating data in persisted game state.
	 */
	@Transient val previouslyPlayedEffectLabels: List<String> = emptyList(),
) : GameState

@Serializable
data class ActiveGameVariables(
	val glyphCount: Int,
	val runesCount: Int,
	val startingRunesCount: Int,
	val stage: Int, // could also have a stage model w/a level model w/a round model
	val level: Int,
	val maxRounds: Int,
	val maxDiscards: Int,
	val handSize: Int,
	val gameLost: Boolean,
	val needsEffectSelection: Boolean = false,
	val needsMidshopSelection: Boolean = false,
)

@Serializable
data class ActiveGameValues(
	val seed: Long,
	val deck: Deck,
	val effects: List<Effect>,
)

@Serializable
data class CurrentRound(
	val round: Int,
	val discardsUsed : Int,
	val enemyHealth: Int,
	val wordsPlayed: List<String>,
	val mutableDeck: Deck,
	val hand: List<Letter>,
	val effects: List<Effect>,
)
