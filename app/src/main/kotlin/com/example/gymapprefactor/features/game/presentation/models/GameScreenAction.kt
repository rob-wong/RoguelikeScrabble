package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.business.models.Letter

sealed class GameScreenAction : Action {
	data class StartPlaying(
		val glyphCount: Int,
		val runesCount: Int,
		val onQuitPressed: () -> Unit,
		val onWordPlayed: (List<String>) -> Unit,
		val onDiscardPressed: () -> Unit,
		val hand: List<Letter>,
		val currentLettersInDeck: Int,
		val maxLettersInDeck: Int,
		val currentRound: Int,
		val maxRounds: Int,
		val discardsRemaining: Int,
		val enemyHealth: Int,
		val enemyMaxHealth: Int,
		val enemyLabel: String,
		val activeGameEffects: List<Effect>,
		val currentRoundEffects: List<Effect>,
		val effectDescriptors: Map<String, EffectDescriptor>,
		val needsEffectSelection: Boolean,
		val effectSelectionEffects: List<Effect>,
		val onEffectSelected: ((Effect) -> Unit)?,
		val onEffectSelectionBackPressed: (() -> Unit)?,
		val needsMidshopSelection: Boolean,
		val midshopOptions: List<MidshopOption>,
		val selectedMidshopOption: MidshopOption?,
		val midshopConfirmButton: com.example.gymapprefactor.common.components.buttons.presentation.ButtonState,
		val onMidshopOptionSelected: ((MidshopOption) -> Unit)?,
		val onMidshopConfirmed: (() -> Unit)?,
		val needsAwakenLetterSelection: Boolean,
		val awakenLetters: List<Letter>,
		val selectedAwakenLetter: Letter?,
		val awakenConfirmButton: com.example.gymapprefactor.common.components.buttons.presentation.ButtonState,
		val onAwakenLetterSelected: ((Letter) -> Unit)?,
		val onAwakenConfirmed: (() -> Unit)?,
	) : GameScreenAction()

	data object None : GameScreenAction()
}
