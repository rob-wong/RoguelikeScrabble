package com.cypherose.features.templateengine.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cypherose.app.util.SpacerUtil
import com.cypherose.features.templateengine.presentation.models.settings.AppInfoItemState
import com.cypherose.ui.theme.OffWhite
import com.cypherose.ui.theme.appCommon

@Composable
fun AppInfoItem(
	state: AppInfoItemState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is AppInfoItemState.Content -> AppInfoItemContent(state, modifier)
		is AppInfoItemState.None -> Unit
	}
}

@Composable
private fun AppInfoItemContent(
	state: AppInfoItemState.Content,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = SpacerUtil.spacer_04)
	) {
		Text(
			text = state.label,
			style = appCommon.copy(fontSize = appCommon.fontSize * 0.7f),
			color = OffWhite.copy(alpha = 0.7f)
		)
		Text(
			text = state.value,
			style = appCommon.copy(fontSize = appCommon.fontSize * 0.8f),
			color = OffWhite
		)
	}
}
