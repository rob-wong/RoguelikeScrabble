package com.cypherose.features.game.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.models.Effect
import com.cypherose.common.components.ui.RoseBackdropLabel
import com.cypherose.ui.theme.appCommon
import com.cypherose.ui.theme.appLegendary
import com.cypherose.ui.theme.appRare
import com.cypherose.ui.theme.appUncommon

@Composable
fun EffectSelectionOption(
	effect: Effect,
	effectDescriptors: Map<String, EffectDescriptor>,
	onClick: () -> Unit,
	hasBackground: Boolean = true,
	modifier: Modifier = Modifier,
) {
	val descriptor = effect.descriptor ?: effectDescriptors[effect.label]
	val textStyle = getEffectTextStyle(descriptor)
	
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp)
			.clickable(onClick = onClick)
			.padding(horizontal = 20.dp, vertical = 20.dp),
		contentAlignment = Alignment.Center
	) {
		RoseBackdropLabel(
			text = effect.label,
			textStyle = textStyle,
			showBackdrop = hasBackground
		)
	}
}

@Composable
private fun getEffectTextStyle(descriptor: EffectDescriptor?): TextStyle {
	return when {
		descriptor == null -> appCommon
		descriptor.type == "fixed_addition" -> appUncommon
		descriptor.type == "multiplication" -> appRare
		descriptor.type == "monetary" -> appLegendary
		else -> appCommon
	}
}

