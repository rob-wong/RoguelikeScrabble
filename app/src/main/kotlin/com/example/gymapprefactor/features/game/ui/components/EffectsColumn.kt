package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.ui.theme.common

@Composable
fun EffectsColumn(
	effects: List<Effect>,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier
			.width(DeviceUtil.getColumnWidthDp(5))
			.height(200.dp)
	) {
		items(
			items = effects,
			key = { effect -> effect.id }
		) { effect ->
			OutlinedText(
				text = effect.label,
				textAlign = TextAlign.Center,
				textStyle = common,
				outlineWidth = 5,
				useGlow = false,
				modifier = Modifier.padding(vertical = 4.dp)
			)
		}
	}
}
