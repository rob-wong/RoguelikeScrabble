package com.cypherose.features.templateengine.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cypherose.business.interfaces.State
import com.cypherose.features.templateengine.presentation.models.settings.AppInfoItemState
import com.cypherose.features.templateengine.presentation.models.settings.ExternalLinkState
import com.cypherose.features.templateengine.presentation.models.settings.SoundEffectsState
import com.cypherose.features.templateengine.ui.settings.AppInfoItem
import com.cypherose.features.templateengine.ui.settings.ExternalLink
import com.cypherose.features.templateengine.ui.settings.SoundEffects
import com.cypherose.features.templateengine.presentation.models.basic.BasicListState
import com.cypherose.features.templateengine.presentation.models.shopcard.ShopCardItemState
import com.cypherose.features.templateengine.ui.item.ShopCard
import com.cypherose.features.templateengine.ui.list.ListRouter

@Composable
fun ContentRouter(
	state: State,
	modifier: Modifier = Modifier
) {
	when (state) {
		is BasicListState -> ListRouter(state, modifier)
		is ShopCardItemState -> ShopCard(state, modifier)
		is AppInfoItemState -> AppInfoItem(state, modifier)
		is SoundEffectsState -> SoundEffects(state, modifier)
		is ExternalLinkState -> ExternalLink(state, modifier)
		else -> Unit
	}
}
