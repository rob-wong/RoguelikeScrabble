package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.ui.OutlinedText
import java.util.Locale
import com.example.gymapprefactor.features.game.presentation.models.components.EnemyHealthBarState
import com.example.gymapprefactor.ui.theme.BlackishGray
import com.example.gymapprefactor.ui.theme.HealthBarGreen
import com.example.gymapprefactor.ui.theme.HealthBarRed
import com.example.gymapprefactor.ui.theme.HealthBarYellow
import com.example.gymapprefactor.ui.theme.OffWhite
import com.example.gymapprefactor.ui.theme.common

@Composable
fun EnemyHealthBarRouter(
	state: EnemyHealthBarState,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is EnemyHealthBarState.Content -> EnemyHealthBarContent(state, modifier)
		is EnemyHealthBarState.None -> Unit
	}
}

@Composable
private fun EnemyHealthBarContent(
	state: EnemyHealthBarState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedText(
			text = state.label,
			textAlign = TextAlign.Center,
			textStyle = common.copy(fontSize = common.fontSize * 0.6f),
			outlineWidth = 3,
			useGlow = false,
		)
		
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(32.dp)
				.border(
					border = BorderStroke(width = 3.dp, color = OffWhite),
					shape = RoundedCornerShape(4.dp)
				)
				.clip(RoundedCornerShape(4.dp))
				.background(BlackishGray)
		) {
			// Health fill bar
			val healthPercentage = if (state.maxHealth > 0) {
				(state.currentHealth.toFloat() / state.maxHealth.toFloat()).coerceIn(0f, 1f)
			} else {
				0f
			}
			
			Box(
				modifier = Modifier
					.fillMaxWidth(healthPercentage)
					.height(32.dp)
					.background(
						color = calculateHealthColor(healthPercentage),
						shape = RoundedCornerShape(4.dp)
					)
			)
			
			// Health text overlay
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(32.dp),
				contentAlignment = Alignment.Center
			) {
				OutlinedText(
					text = formatHealthText(state.currentHealth),
					textAlign = TextAlign.Center,
					textStyle = common.copy(fontSize = common.fontSize * 0.5f),
					outlineWidth = 2,
					useGlow = false,
				)
			}
		}
	}
}

private fun calculateHealthColor(percentage: Float): Color {
	return when {
		percentage > 0.6f -> HealthBarGreen
		percentage > 0.3f -> HealthBarYellow
		else -> HealthBarRed
	}
}

private fun formatHealthText(current: Int): String {
	return formatNumber(current)
}

private fun formatNumber(number: Int): String {
	return when {
		number >= 1_000_000 -> {
			val millions = number / 1_000_000.0
			val formatted = if (millions % 1.0 == 0.0) {
				millions.toInt().toString()
			} else {
				String.format(Locale.US, "%.1f", millions)
			}
			"${formatted}M"
		}
		number >= 1_000 -> {
			val thousands = number / 1_000.0
			val formatted = if (thousands % 1.0 == 0.0) {
				thousands.toInt().toString()
			} else {
				String.format(Locale.US, "%.1f", thousands)
			}
			"${formatted}K"
		}
		else -> number.toString()
	}
}

@Preview
@Composable
private fun EnemyHealthBarRouterPreview() {
	EnemyHealthBarRouter(
		state = EnemyHealthBarState.Content(
			label = "ENEMY",
			currentHealth = 15000,
			maxHealth = 30000
		)
	)
}
