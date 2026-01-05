package com.example.gymapprefactor.features.templateengine.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.features.templateengine.presentation.models.basic.BasicListState
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemState
import com.example.gymapprefactor.features.templateengine.ui.item.ShopCard
import com.example.gymapprefactor.features.templateengine.ui.list.ListRouter

@Composable
fun ContentRouter(
	state: State,
	modifier: Modifier = Modifier
) {
	when (state) {
		is BasicListState -> ListRouter(state, modifier)
		is ShopCardItemState -> ShopCard(state, modifier)
		else -> Unit
	}
}
