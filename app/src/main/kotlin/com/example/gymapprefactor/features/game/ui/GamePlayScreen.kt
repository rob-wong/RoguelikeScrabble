package com.example.gymapprefactor.features.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.presentation.BagState
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
import com.example.gymapprefactor.common.components.ui.ScreenBackgroundRouter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState.DraggableLetter
import com.example.gymapprefactor.features.game.presentation.models.InputButtonState
import com.example.gymapprefactor.features.game.presentation.models.components.DiscardsRemainingState
import com.example.gymapprefactor.features.game.presentation.viewmodel.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.components.RoundsRemainingState

@Composable
fun GamePlayScreen(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	scoreBreakdown: ScoreAnimationPayload?,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier) {
		ScreenBackgroundRouter(ScreenBackgroundState.Game)
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			ResourceBarRouter(state.resourceBar)
			Column(Modifier.fillMaxSize()) {
				Spacer(Modifier.height(50.dp))
				LetterBoard(
					state = state,
					invalidWordTrigger = invalidWordTrigger,
					onInvalidWordConsumed = onInvalidWordConsumed,
					scoreBreakdown = scoreBreakdown,
					onScoreAnimationConsumed = onScoreAnimationConsumed,
					onScoreAnimationComplete = onScoreAnimationComplete,
				)
			}
		}
		ButtonRouter(
			state.quitButton, Modifier
				.padding(top = 10.dp, end = 10.dp)
				.align(Alignment.TopEnd)
				.size(40.dp)
		)
	}
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
			)
		),
		invalidWordTrigger = false,
		onInvalidWordConsumed = { },
		scoreBreakdown = null,
		onScoreAnimationConsumed = { },
		onScoreAnimationComplete = { },
	)
}
