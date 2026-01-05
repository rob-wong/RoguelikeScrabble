package com.example.gymapprefactor.features.templateengine.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemState
import com.example.gymapprefactor.ui.theme.Typography

@Composable
fun ShopCard(
	state: ShopCardItemState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is ShopCardItemState.Content -> ShopCardContent(state, modifier)
		is ShopCardItemState.None -> Unit
	}
}

@Composable
private fun ShopCardContent(
	state: ShopCardItemState.Content,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.background(Color.White)
			.padding(vertical = 30.dp)
	) {
		state.price?.let { price ->
			Text(
				text = "Price: ${price.type} ${price.amount}",
				color = Color.Black,
				style = Typography.bodyMedium
			)
		}
		state.product?.let { product ->
			Text(
				text = "Product: ${product.type}${product.amount?.let { " $it" } ?: ""}${product.decktype?.let { " ($it)" } ?: ""}",
				color = Color.Black,
				style = Typography.bodyMedium
			)
		}
		state.description?.let { description ->
			Text(
				text = "Description: $description",
				color = Color.Black,
				style = Typography.bodyMedium
			)
		}
	}
}
