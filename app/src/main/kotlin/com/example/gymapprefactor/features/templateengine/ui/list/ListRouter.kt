package com.example.gymapprefactor.features.templateengine.ui.list

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymapprefactor.features.templateengine.presentation.models.basic.BasicListState
import com.example.gymapprefactor.features.templateengine.ui.ContentRouter

@Composable
fun ListRouter(
	state: BasicListState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is BasicListState.Content -> BasicListContent(state, modifier)
		is BasicListState.None -> Unit
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BasicListContent(
	state: BasicListState.Content,
	modifier: Modifier = Modifier,
) {
	FlowRow(modifier = modifier) {
		state.items.forEach { item ->
			ContentRouter(item)
		}
	}
}
