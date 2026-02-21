package com.cypherose.features.templateengine.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cypherose.app.util.SpacerUtil
import com.cypherose.features.templateengine.presentation.models.settings.SoundEffectsState
import com.cypherose.ui.theme.OffWhite
import com.cypherose.ui.theme.Typography

@Composable
fun SoundEffects(
	state: SoundEffectsState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is SoundEffectsState.Content -> SoundEffectsContent(state, modifier)
		is SoundEffectsState.None -> Unit
	}
}

@Composable
private fun SoundEffectsContent(
	state: SoundEffectsState.Content,
	modifier: Modifier = Modifier
) {
	var volume by remember { mutableFloatStateOf(state.volume) }
	
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = SpacerUtil.spacer_04)
	) {
		Text(
			text = "${state.volumeLabel}${(volume * 100).toInt()}%",
			style = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * 0.7f),
			color = OffWhite.copy(alpha = 0.7f),
			modifier = Modifier.fillMaxWidth()
		)
		Slider(
			value = volume,
			onValueChange = { volume = it },
			valueRange = 0f..1f,
			modifier = Modifier.fillMaxWidth()
		)
		Text(
			text = state.placeholderText,
			style = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * 0.6f),
			color = OffWhite.copy(alpha = 0.5f),
			modifier = Modifier.fillMaxWidth()
		)
	}
}
