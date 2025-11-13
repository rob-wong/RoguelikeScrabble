package com.example.gymapprefactor.features.game.presentation.models

import androidx.compose.ui.geometry.Offset
import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.BagState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.features.game.presentation.models.components.DiscardsRemainingState
import com.example.gymapprefactor.features.game.presentation.models.components.EnemyHealthBarState
import com.example.gymapprefactor.features.game.presentation.models.components.RoundsRemainingState

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
	): GameScreenState()

	data object None : GameScreenState()

	data class DraggableLetter(
		val id: String,
		val letterState: LetterState,
		var offset: Offset = Offset.Zero
	)
}
