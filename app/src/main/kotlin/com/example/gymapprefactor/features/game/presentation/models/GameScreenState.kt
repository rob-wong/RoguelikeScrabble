package com.example.gymapprefactor.features.game.presentation.models

import androidx.compose.ui.geometry.Offset
import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class GameScreenState : State {
	data class Playing(
		val resourceBar: ResourceBarState,
		val quitButton: ButtonState,
		val playButton: InputButtonState,
		val discardButton: ButtonState,
		val letters: List<DraggableLetter>,
	): GameScreenState()

	data object None : GameScreenState()

	data class DraggableLetter(
		val id: String,
		val letterState: LetterState,
		var offset: Offset = Offset.Zero
	)
}
