package com.example.gymapprefactor.features.game.presentation.state

import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.BagState
import com.example.gymapprefactor.features.game.presentation.models.InputButtonState
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState.DraggableLetter
import com.example.gymapprefactor.features.game.presentation.models.components.DiscardsRemainingState
import com.example.gymapprefactor.features.game.presentation.models.components.EnemyHealthBarState
import com.example.gymapprefactor.features.game.presentation.models.components.RoundsRemainingState
import kotlinx.coroutines.flow.MutableStateFlow

class GameScreenReducerImpl : GameScreenReducer {
	override val state = MutableStateFlow<GameScreenState>(GameScreenState.None)

	override suspend fun update(action: GameScreenAction) {
		when (action) {
			is GameScreenAction.StartPlaying -> mapPlayingState(action)
			is GameScreenAction.None -> Unit
		}
	}

	private fun mapPlayingState(action: GameScreenAction.StartPlaying) {
		state.value = GameScreenState.Playing(
			resourceBar = mapResourceBar(action),
			quitButton = mapQuitButton(action),
			letters = action.hand.map {
				DraggableLetter(
					id = it.id,
					letterState =
						LetterState.Display(
							type = DeckType.Default,
							letter = it.letter.toUpperCase(),
							level = it.level
						)
				)
			},
			playButton = mapPlayButton(action),
			discardButton = mapDiscardButton(action),
			bag = mapBag(action),
			roundsRemainingState = mapRoundsRemaining(action),
			discardsRemainingState = mapDiscardsRemaining(action),
			enemyHealthBarState = mapEnemyHealthBar(action),
			activeGameEffects = action.activeGameEffects,
			currentRoundEffects = action.currentRoundEffects,
			effectDescriptors = action.effectDescriptors,
			needsEffectSelection = action.needsEffectSelection,
			effectSelectionEffects = action.effectSelectionEffects,
			onEffectSelected = action.onEffectSelected,
			onEffectSelectionBackPressed = action.onEffectSelectionBackPressed,
			needsMidshopSelection = action.needsMidshopSelection,
			midshopOptions = action.midshopOptions,
			selectedMidshopOption = action.selectedMidshopOption,
			midshopConfirmButton = mapMidshopConfirmButton(action),
			onMidshopOptionSelected = action.onMidshopOptionSelected,
			onMidshopConfirmed = action.onMidshopConfirmed,
			needsAwakenLetterSelection = action.needsAwakenLetterSelection,
			awakenLetters = action.awakenLetters,
			selectedAwakenLetter = action.selectedAwakenLetter,
			awakenConfirmButton = mapAwakenConfirmButton(action),
			onAwakenLetterSelected = action.onAwakenLetterSelected,
			onAwakenConfirmed = action.onAwakenConfirmed,
		)
	}

	private fun mapResourceBar(action: GameScreenAction.StartPlaying): ResourceBarState {
		return ResourceBarState.Content(
			runeState = ResourceState.Content(
				amount = action.runesCount.toString(),
				icon = ImageState.RuneIcon
			),
			glyphState = ResourceState.Content(
				amount = action.glyphCount.toString(),
				icon = ImageState.GlyphIcon
			),
		)
	}

	private fun mapQuitButton(action: GameScreenAction.StartPlaying): ButtonState {
		return IconButtonState.Content(
			onClick = action.onQuitPressed,
			image = ImageState.QuitIcon,
		)
	}

	private fun mapPlayButton(action: GameScreenAction.StartPlaying): InputButtonState {
		return InputButtonState.InputWordButton(
			onClick = action.onWordPlayed,
			image = ImageState.ConfirmIcon,
		)
	}

	private fun mapDiscardButton(action: GameScreenAction.StartPlaying): ButtonState {
		return IconButtonState.Content(
			onClick = action.onDiscardPressed,
			image = ImageState.DiscardIcon,
		)
	}

	private fun mapBag(action: GameScreenAction.StartPlaying): BagState {
		return BagState.Content(
			image = ImageState.BasicBagIcon, // mapper when a bag exists
			currentLetters = action.currentLettersInDeck,
			maxLetters = action.maxLettersInDeck
		)
	}

	private fun mapRoundsRemaining(action: GameScreenAction.StartPlaying): RoundsRemainingState {
		return RoundsRemainingState.Content(
			image = ImageState.PlaysLeftIcon,
			currentRound = action.currentRound,
			maxRounds = action.maxRounds
		)
	}

	private fun mapDiscardsRemaining(action: GameScreenAction.StartPlaying): DiscardsRemainingState {
		return DiscardsRemainingState.Content(
			image = ImageState.DiscardsLeftIcon,
			remaining = action.discardsRemaining
		)
	}

	private fun mapEnemyHealthBar(action: GameScreenAction.StartPlaying): EnemyHealthBarState {
		return EnemyHealthBarState.Content(
			label = action.enemyLabel,
			currentHealth = action.enemyHealth,
			maxHealth = action.enemyMaxHealth
		)
	}
	
	private fun mapMidshopConfirmButton(action: GameScreenAction.StartPlaying): ButtonState {
		return if (action.needsMidshopSelection && action.selectedMidshopOption != null) {
			IconButtonState.Content(
				onClick = action.onMidshopConfirmed ?: {},
				image = ImageState.ConfirmIcon
			)
		} else {
			IconButtonState.None
		}
	}
	
	private fun mapAwakenConfirmButton(action: GameScreenAction.StartPlaying): ButtonState {
		return if (action.needsAwakenLetterSelection && action.selectedAwakenLetter != null) {
			IconButtonState.Content(
				onClick = action.onAwakenConfirmed ?: {},
				image = ImageState.ConfirmIcon
			)
		} else {
			IconButtonState.None
		}
	}
}
