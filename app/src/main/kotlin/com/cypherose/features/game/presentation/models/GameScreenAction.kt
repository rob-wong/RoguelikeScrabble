package com.cypherose.features.game.presentation.models

import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.interfaces.Action
import com.cypherose.business.models.Effect
import com.cypherose.business.models.Letter
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopLetterSelectionState

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
		val midshopConfirmButton: com.cypherose.common.components.buttons.presentation.ButtonState,
		val onMidshopOptionSelected: ((MidshopOption) -> Unit)?,
		val onMidshopConfirmed: (() -> Unit)?,
		val awakenLetterSelection: MidshopLetterSelectionState?,
		val expungeLetterSelection: MidshopLetterSelectionState?,
		val previouslyPlayedEffects: List<String>,
		val onPreviouslyPlayedPressed: () -> Unit,
		val previouslyPlayedOverlayVisible: Boolean,
	) : GameScreenAction()

	data object None : GameScreenAction()
}
