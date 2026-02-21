package com.cypherose.features.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cypherose.app.util.DevicePreviews
import com.cypherose.business.models.DefaultEffect
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.common.components.presentation.BagState
import com.cypherose.common.components.presentation.DeckType
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.common.components.ui.ResourceBarRouter
import com.cypherose.features.game.presentation.models.animation.EffectAnimationPayload
import com.cypherose.features.game.presentation.models.GameScreenState
import com.cypherose.features.game.presentation.models.GameScreenState.DraggableLetter
import com.cypherose.features.game.presentation.models.InputButtonState
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload
import com.cypherose.features.game.presentation.models.animation.ScoreAnimationPayload
import com.cypherose.features.game.presentation.models.animation.GlyphAnimationPayload
import com.cypherose.features.game.presentation.models.components.DiscardsRemainingState
import com.cypherose.features.game.presentation.models.components.EnemyHealthBarState
import com.cypherose.features.game.presentation.models.components.RoundsRemainingState
import com.cypherose.business.models.Letter
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.features.game.ui.components.EnemyHealthBarRouter
import com.cypherose.features.game.ui.overlays.EffectSelectionOverlay
import com.cypherose.features.game.ui.overlays.GlyphAnimationOverlay
import com.cypherose.features.game.ui.overlays.midshopresults.MidshopResultOverlay
import com.cypherose.features.game.ui.overlays.MidshopSelectionOverlay
import kotlinx.coroutines.delay

@Composable
fun GamePlayScreen(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	scoreBreakdown: ScoreAnimationPayload?,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	effectAnimations: List<EffectAnimationPayload>?,
	onEffectAnimationConsumed: () -> Unit,
	onEffectAnimationComplete: () -> Unit,
	glyphAnimation: GlyphAnimationPayload?,
	onGlyphAnimationComplete: () -> Unit,
	midshopResult: MidshopResultPayload?,
	onMidshopResultAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier,
) {
	// Track whether to show overlay with delay
	var showOverlay by remember { mutableStateOf(false) }
	
	LaunchedEffect(state.needsEffectSelection) {
		if (state.needsEffectSelection) {
			// Wait 1 second after enemy health reaches 0 before showing overlay
			delay(1000L)
			showOverlay = true
		} else {
			showOverlay = false
		}
	}
	
	Box(modifier.fillMaxSize()) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			ResourceBarRouter(state.resourceBar)
			EnemyHealthBarRouter(state.enemyHealthBarState)
			Column(Modifier.fillMaxSize()) {
				Spacer(Modifier.height(50.dp))
				LetterBoard(
					state = state,
					invalidWordTrigger = invalidWordTrigger,
					onInvalidWordConsumed = onInvalidWordConsumed,
					levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
					onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed,
					scoreBreakdown = scoreBreakdown,
					onScoreAnimationConsumed = onScoreAnimationConsumed,
					onScoreAnimationComplete = onScoreAnimationComplete,
					effectAnimations = effectAnimations,
					onEffectAnimationConsumed = onEffectAnimationConsumed,
					onEffectAnimationComplete = onEffectAnimationComplete,
				)
			}
		}
		
		// Glyph drop animation overlay - centered on screen, above main content
		GlyphAnimationOverlay(
			glyphAnimation = glyphAnimation,
			onGlyphAnimationComplete = onGlyphAnimationComplete,
			modifier = Modifier
		)
		
		ButtonRouter(
			state.quitButton, Modifier
				.padding(top = 10.dp, end = 10.dp)
				.align(Alignment.TopEnd)
				.size(40.dp)
		)
		
		GameOverlays(
			state = state,
			showOverlay = showOverlay,
			midshopResult = midshopResult,
			onMidshopResultAnimationComplete = onMidshopResultAnimationComplete
		)
	}
}

@Composable
private fun GameOverlays(
	state: GameScreenState.Playing,
	showOverlay: Boolean,
	midshopResult: MidshopResultPayload?,
	onMidshopResultAnimationComplete: () -> Unit
) {
	EffectSelectionOverlayIfNeeded(
		state = state,
		showOverlay = showOverlay
	)
	
	MidshopOverlays(
		state = state,
		midshopResult = midshopResult,
		onMidshopResultAnimationComplete = onMidshopResultAnimationComplete
	)
}

@Composable
private fun EffectSelectionOverlayIfNeeded(
	state: GameScreenState.Playing,
	showOverlay: Boolean
) {
	if (!shouldShowEffectSelectionOverlay(state, showOverlay)) {
		return
	}
	
	EffectSelectionOverlay(
		effects = state.effectSelectionEffects,
		effectDescriptors = state.effectDescriptors,
		onEffectSelected = requireNotNull(state.onEffectSelected),
		onBackPressed = requireNotNull(state.onEffectSelectionBackPressed),
	)
}

private fun shouldShowEffectSelectionOverlay(
	state: GameScreenState.Playing,
	showOverlay: Boolean
): Boolean {
	return showOverlay &&
		state.needsEffectSelection &&
		state.onEffectSelected != null &&
		state.onEffectSelectionBackPressed != null
}

@Composable
private fun MidshopOverlays(
	state: GameScreenState.Playing,
	midshopResult: MidshopResultPayload?,
	onMidshopResultAnimationComplete: () -> Unit
) {
	if (midshopResult != null) {
		MidshopResultOverlayContent(
			result = midshopResult,
			state = state,
			onAnimationComplete = onMidshopResultAnimationComplete
		)
	} else if (shouldShowMidshopSelectionOverlay(state)) {
		MidshopSelectionOverlayContent(state = state)
	}
}

@Composable
private fun MidshopResultOverlayContent(
	result: MidshopResultPayload,
	state: GameScreenState.Playing,
	onAnimationComplete: () -> Unit
) {
	val letterSelectionProps = extractLetterSelectionProps(result, state)
	val isAwaken = result is MidshopResultPayload.Awaken
	
	MidshopResultOverlay(
		result = result,
		onAnimationComplete = onAnimationComplete,
		selectedAwakenLetter = if (isAwaken) letterSelectionProps?.selectedLetter else null,
		awakenConfirmButton = if (isAwaken) letterSelectionProps?.confirmButton else null,
		onAwakenLetterSelected = if (isAwaken) letterSelectionProps?.onLetterSelected else null,
		onAwakenConfirmed = if (isAwaken) letterSelectionProps?.onConfirmed else null,
		selectedExpungeLetter = if (!isAwaken) letterSelectionProps?.selectedLetter else null,
		expungeConfirmButton = if (!isAwaken) letterSelectionProps?.confirmButton else null,
		onExpungeLetterSelected = if (!isAwaken) letterSelectionProps?.onLetterSelected else null,
		onExpungeConfirmed = if (!isAwaken) letterSelectionProps?.onConfirmed else null
	)
}

private data class LetterSelectionProps(
	val selectedLetter: Letter?,
	val confirmButton: ButtonState,
	val onLetterSelected: ((Letter) -> Unit)?,
	val onConfirmed: (() -> Unit)?
)

private fun extractLetterSelectionProps(
	result: MidshopResultPayload,
	state: GameScreenState.Playing
): LetterSelectionProps? {
	val selectionState = when (result) {
		is MidshopResultPayload.Awaken -> state.awakenLetterSelection
		is MidshopResultPayload.Expunge -> state.expungeLetterSelection
		else -> null
	}
	
	return selectionState?.let {
		LetterSelectionProps(
			selectedLetter = it.selectedLetter,
			confirmButton = it.confirmButton,
			onLetterSelected = it.onLetterSelected,
			onConfirmed = it.onConfirmed
		)
	}
}

@Composable
private fun MidshopSelectionOverlayContent(
	state: GameScreenState.Playing
) {
	MidshopSelectionOverlay(
		options = state.midshopOptions,
		selectedOption = state.selectedMidshopOption,
		confirmButton = state.midshopConfirmButton,
		onOptionSelected = requireNotNull(state.onMidshopOptionSelected),
	)
}

private fun shouldShowMidshopSelectionOverlay(
	state: GameScreenState.Playing
): Boolean {
	return state.needsMidshopSelection &&
		state.midshopOptions.isNotEmpty() &&
		state.onMidshopOptionSelected != null &&
		state.onMidshopConfirmed != null
}

@SuppressWarnings("LongMethod")
@DevicePreviews
@Composable
private fun GamePlayScreenPreview() {
	GamePlayScreen(
		GameScreenState.Playing(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = "30",
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.Content(
					amount = "20",
					icon = ImageState.GlyphIcon
				),
			),
			quitButton = IconButtonState.Content({ }, ImageState.QuitIcon),
			letters = listOf(
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'A', level = 1)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'B', level = 2)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'C', level = 1)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'D', level = 4)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'E', level = 1)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'F', level = 1)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'G', level = 5)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'H', level = 1)
				),
				DraggableLetter(
					"",
					LetterState.Display(type = DeckType.Default, letter = 'I', level = 1)
				),
			),
			playButton = InputButtonState.InputWordButton(
				onClick = { _ -> },
				image = ImageState.ConfirmIcon
			),
			discardButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.DiscardIcon
			),
			bag = BagState.Content(
				image = ImageState.BasicBagIcon,
				currentLetters = 10,
				maxLetters = 20
			),
			roundsRemainingState = RoundsRemainingState.Content(
				image = ImageState.PlaysLeftIcon,
				currentRound = 5,
				maxRounds = 10
			),
			discardsRemainingState = DiscardsRemainingState.Content(
				image = ImageState.DiscardsLeftIcon,
				remaining = 3
			),
			enemyHealthBarState = EnemyHealthBarState.Content(
				label = "ENEMY",
				currentHealth = 15000,
				maxHealth = 30000
			),
			activeGameEffects = listOf(
				DefaultEffect(
					id = "f",
					label = "EFFECT"
				)
			),
			currentRoundEffects = emptyList(),
			effectDescriptors = emptyMap(),
			needsEffectSelection = false,
			effectSelectionEffects = emptyList(),
			onEffectSelected = null,
			onEffectSelectionBackPressed = null,
			needsMidshopSelection = false,
			midshopOptions = emptyList(),
			selectedMidshopOption = null,
			midshopConfirmButton = IconButtonState.None,
			onMidshopOptionSelected = null,
			onMidshopConfirmed = null,
			awakenLetterSelection = null,
			expungeLetterSelection = null,
		),
		invalidWordTrigger = false,
		onInvalidWordConsumed = { },
		levelAdvanceShakeTrigger = false,
		onLevelAdvanceShakeConsumed = { },
		scoreBreakdown = null,
		onScoreAnimationConsumed = { },
		onScoreAnimationComplete = { },
		effectAnimations = null,
		onEffectAnimationConsumed = { },
		onEffectAnimationComplete = { },
		glyphAnimation = null,
		onGlyphAnimationComplete = { },
		midshopResult = null,
		onMidshopResultAnimationComplete = { },
	)
}
