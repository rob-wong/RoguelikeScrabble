package com.cypherose.features.templateengine.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cypherose.app.util.SpacerUtil
import com.cypherose.features.templateengine.presentation.models.settings.ExternalLinkState
import com.cypherose.ui.theme.OffWhite
import com.cypherose.ui.theme.Typography

@Composable
fun ExternalLink(
	state: ExternalLinkState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is ExternalLinkState.Content -> ExternalLinkContent(state, modifier)
		is ExternalLinkState.None -> Unit
	}
}

@Composable
private fun ExternalLinkContent(
	state: ExternalLinkState.Content,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = SpacerUtil.spacer_04)
	) {
		Text(
			text = state.label,
			style = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * 0.8f),
			color = OffWhite
		)
		Text(
			text = state.status,
			style = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * 0.6f),
			color = OffWhite.copy(alpha = 0.5f)
		)
	}
}
