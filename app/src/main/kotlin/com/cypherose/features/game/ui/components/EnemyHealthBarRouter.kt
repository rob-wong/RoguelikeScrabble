@file:Suppress("TooManyFunctions")
package com.cypherose.features.game.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.ui.OutlinedText
import com.cypherose.features.game.presentation.models.components.EnemyHealthBarState
import com.cypherose.ui.theme.BlackishGray
import com.cypherose.ui.theme.HealthBarGreen
import com.cypherose.ui.theme.HealthBarRed
import com.cypherose.ui.theme.HealthBarYellow
import com.cypherose.ui.theme.OffWhite
import com.cypherose.ui.theme.common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

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
	var previousHealth by remember(state.maxHealth) { mutableIntStateOf(0) }
	var previousMaxHealth by remember { mutableIntStateOf(state.maxHealth) }
	
	LaunchedEffect(state.maxHealth) {
		if (state.maxHealth != previousMaxHealth) {
			previousHealth = 0
			previousMaxHealth = state.maxHealth
		}
	}
	
	val targetHealthPercentage = calculateHealthPercentage(
		currentHealth = state.currentHealth,
		maxHealth = state.maxHealth
	)
	
	val animatedHealthPercentage = key(state.maxHealth) {
		remember { Animatable(0f) }
	}
	
	LaunchedEffect(targetHealthPercentage) {
		animatedHealthPercentage.animateTo(
			targetValue = targetHealthPercentage,
			animationSpec = tween(
				durationMillis = 500,
				easing = LinearEasing
			)
		)
	}
	
	val shakeOffset = remember { Animatable(0f) }
	val flashAlpha = remember { Animatable(0f) }
	val scope = rememberCoroutineScope()
	
	HandleDamageAnimations(
		currentHealth = state.currentHealth,
		previousHealth = previousHealth,
		shakeOffset = shakeOffset,
		flashAlpha = flashAlpha,
		scope = scope,
		onHealthUpdated = { previousHealth = it }
	)
	
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp)
			.offset(x = shakeOffset.value.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedText(
			text = state.label,
			textAlign = TextAlign.Center,
			textStyle = common.copy(fontSize = common.fontSize * 0.8f),
			outlineWidth = 3,
			useGlow = false,
		)
		
		HealthBarBox(
			animatedHealthPercentage = animatedHealthPercentage.value,
			flashAlpha = flashAlpha.value,
			currentHealth = state.currentHealth
		)
	}
}

@Composable
private fun HealthBarBox(
	animatedHealthPercentage: Float,
	flashAlpha: Float,
	currentHealth: Int
) {
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
		Box(
			modifier = Modifier
				.fillMaxWidth(animatedHealthPercentage)
				.height(32.dp)
				.background(
					color = calculateHealthColor(animatedHealthPercentage),
					shape = RoundedCornerShape(4.dp)
				)
		)
		
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(32.dp)
				.alpha(flashAlpha)
				.background(
					color = HealthBarRed,
					shape = RoundedCornerShape(4.dp)
				)
		)
		
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(32.dp),
			contentAlignment = Alignment.Center
		) {
			OutlinedText(
				text = formatHealthText(currentHealth),
				textAlign = TextAlign.Center,
				textStyle = common.copy(fontSize = common.fontSize * 0.5f),
				outlineWidth = 2,
				useGlow = false,
			)
		}
	}
}

@Composable
private fun HandleDamageAnimations(
	currentHealth: Int,
	previousHealth: Int,
	shakeOffset: Animatable<Float, *>,
	flashAlpha: Animatable<Float, *>,
	scope: CoroutineScope,
	onHealthUpdated: (Int) -> Unit
) {
	LaunchedEffect(currentHealth) {
		val healthDecreased = currentHealth < previousHealth
		if (healthDecreased) {
			scope.launch {
				coroutineScope {
					launch { animateShake(shakeOffset) }
					launch { animateFlash(flashAlpha) }
				}
			}
		}
		onHealthUpdated(currentHealth)
	}
}

private suspend fun animateShake(shakeOffset: Animatable<Float, *>) {
	val shakeDuration = 300
	val shakeAmount = 6f
	val shakeCycles = 3
	val cycleDuration = shakeDuration / shakeCycles
	
	shakeOffset.animateTo(
		targetValue = shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration / 2,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = -shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = 0f,
		animationSpec = tween(
			durationMillis = cycleDuration / 2,
			easing = LinearEasing
		)
	)
}

private suspend fun animateFlash(flashAlpha: Animatable<Float, *>) {
	flashAlpha.animateTo(
		targetValue = 0.6f,
		animationSpec = tween(
			durationMillis = 50,
			easing = LinearEasing
		)
	)
	flashAlpha.animateTo(
		targetValue = 0f,
		animationSpec = tween(
			durationMillis = 250,
			easing = LinearEasing
		)
	)
}

private fun calculateHealthPercentage(currentHealth: Int, maxHealth: Int): Float {
	return if (maxHealth > 0) {
		(currentHealth.toFloat() / maxHealth.toFloat()).coerceIn(0f, 1f)
	} else {
		0f
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
	Column {
		EnemyHealthBarRouter(
			state = EnemyHealthBarState.Content(
				label = "ENEMY",
				currentHealth = 15000,
				maxHealth = 30000
			)
		)
		EnemyHealthBarRouter(
			state = EnemyHealthBarState.Content(
				label = "ENEMY",
				currentHealth = 15000000,
				maxHealth = 30000000
			)
		)
	}
}
