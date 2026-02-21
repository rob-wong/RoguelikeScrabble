package com.cypherose.features.game.presentation.models

import androidx.compose.ui.geometry.Offset
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.interfaces.State
import com.cypherose.business.models.Effect
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.BagState
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.features.game.presentation.models.components.DiscardsRemainingState
import com.cypherose.features.game.presentation.models.components.EnemyHealthBarState
import com.cypherose.features.game.presentation.models.components.RoundsRemainingState
import com.cypherose.features.game.presentation.models.midshop.MidshopLetterSelectionState
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopOptionState

sealed class GameScreenState : State {
	data class Playing(
		val resourceBar: ResourceBarState,
		val quitButton: ButtonState,
		val playButton: InputButtonState,
		val discardButton: ButtonState,
		val letters: List<DraggableLetter>,
		val bag: BagState,
		val roundsRemainingState: RoundsRemainingState,
		val discardsRemainingState: DiscardsRemainingState,
		val enemyHealthBarState: EnemyHealthBarState,
		val activeGameEffects: List<Effect>,
		val currentRoundEffects: List<Effect>,
		val effectDescriptors: Map<String, EffectDescriptor>,
		val needsEffectSelection: Boolean,
		val effectSelectionEffects: List<Effect>,
		val onEffectSelected: ((Effect) -> Unit)?,
		val onEffectSelectionBackPressed: (() -> Unit)?,
		val needsMidshopSelection: Boolean,
		val midshopOptions: List<MidshopOptionState>,
		val selectedMidshopOption: MidshopOptionState?,
		val midshopConfirmButton: ButtonState,
		val onMidshopOptionSelected: ((MidshopOption) -> Unit)?,
		val onMidshopConfirmed: (() -> Unit)?,
		val awakenLetterSelection: MidshopLetterSelectionState?,
		val expungeLetterSelection: MidshopLetterSelectionState?,
	): GameScreenState()

	data object None : GameScreenState()

	data class DraggableLetter(
		val id: String,
		val letterState: LetterState,
		var offset: Offset = Offset.Zero
	)
}
