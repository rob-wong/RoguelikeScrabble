package com.example.gymapprefactor.features.game.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.features.game.ui.components.EffectSelectionOption

@Composable
fun EffectSelectionOverlay(
	effects: List<Effect>,
	effectDescriptors: Map<String, EffectDescriptor>,
	onEffectSelected: (Effect) -> Unit,
	onBackPressed: () -> Unit,
	modifier: Modifier = Modifier,
) {
	BackHandler {
		onBackPressed()
	}
	
	EffectSelectionContent(
		effects = effects,
		effectDescriptors = effectDescriptors,
		onEffectSelected = onEffectSelected,
		modifier = modifier
	)
}

@Composable
private fun EffectSelectionContent(
	effects: List<Effect>,
	effectDescriptors: Map<String, EffectDescriptor>,
	onEffectSelected: (Effect) -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		// Dark overlay to make everything else darker and unselectable
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.7f))
		)
		
		// Selection UI on top
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(32.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(vertical = 32.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				items(
					items = effects,
					key = { effect ->
						effect.id
					}
				) { effect ->
					EffectSelectionOption(
						effect = effect,
						effectDescriptors = effectDescriptors,
						onClick = { onEffectSelected(effect) }
					)
				}
			}
		}
	}
}

