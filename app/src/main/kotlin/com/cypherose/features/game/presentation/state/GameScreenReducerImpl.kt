package com.cypherose.features.game.presentation.state

import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.presentation.BagState
import com.cypherose.common.components.presentation.DeckType
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.features.game.presentation.models.GameScreenAction
import com.cypherose.features.game.presentation.models.GameScreenState
import com.cypherose.features.game.presentation.models.GameScreenState.DraggableLetter
import com.cypherose.features.game.presentation.models.InputButtonState
import com.cypherose.features.game.presentation.models.components.DiscardsRemainingState
import com.cypherose.features.game.presentation.models.components.EnemyHealthBarState
import com.cypherose.features.game.presentation.models.components.RoundsRemainingState
import com.cypherose.features.game.presentation.models.midshop.MidshopLetterSelectionState
import com.cypherose.features.game.presentation.state.PreviouslyPlayedEffectsMapper.Param
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameScreenReducerImpl @Inject constructor(
	private val midshopOptionStateMapper: MidshopOptionStateMapper,
	private val previouslyPlayedEffectsMapper: PreviouslyPlayedEffectsMapper,
) : GameScreenReducer {
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
			midshopOptions = action.midshopOptions.map { midshopOptionStateMapper.map(it) },
			selectedMidshopOption = action.selectedMidshopOption?.let { midshopOptionStateMapper.map(it) },
			midshopConfirmButton = mapMidshopConfirmButton(action),
			onMidshopOptionSelected = action.onMidshopOptionSelected,
			onMidshopConfirmed = action.onMidshopConfirmed,
			awakenLetterSelection = action.awakenLetterSelection?.let { awakenState ->
				awakenState.copy(
					confirmButton = mapAwakenConfirmButton(awakenState)
				)
			},
			expungeLetterSelection = action.expungeLetterSelection?.let { expungeState ->
				expungeState.copy(
					confirmButton = mapExpungeConfirmButton(expungeState)
				)
			},
			previouslyPlayedEffects = previouslyPlayedEffectsMapper.map(
				Param(
					effectLabels = action.previouslyPlayedEffects,
					hand = action.hand
				)
			),
			onPreviouslyPlayedPressed = action.onPreviouslyPlayedPressed,
			previouslyPlayedOverlayVisible = action.previouslyPlayedOverlayVisible,
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
	
	private fun mapAwakenConfirmButton(awakenState: MidshopLetterSelectionState): ButtonState {
		return if (awakenState.needsSelection && awakenState.selectedLetter != null) {
			IconButtonState.Content(
				onClick = awakenState.onConfirmed ?: {},
				image = ImageState.ConfirmIcon
			)
		} else {
			IconButtonState.None
		}
	}
	
	private fun mapExpungeConfirmButton(expungeState: MidshopLetterSelectionState): ButtonState {
		return if (expungeState.needsSelection && expungeState.selectedLetter != null) {
			IconButtonState.Content(
				onClick = expungeState.onConfirmed ?: {},
				image = ImageState.ConfirmIcon
			)
		} else {
			IconButtonState.None
		}
	}
}
