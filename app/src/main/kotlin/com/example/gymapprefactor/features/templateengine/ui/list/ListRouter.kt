package com.example.gymapprefactor.features.templateengine.ui.list

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

@Composable
private fun BasicListContent(
	state: BasicListState.Content,
	modifier: Modifier = Modifier,
) {
	LazyRow(modifier = modifier) {
		items(state.items) { item ->
			ContentRouter(item)
		}
	}
}
