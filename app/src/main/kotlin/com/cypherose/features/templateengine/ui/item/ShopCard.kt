package com.cypherose.features.templateengine.ui.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.features.templateengine.presentation.models.shopcard.ProductContent
import com.cypherose.features.templateengine.presentation.models.shopcard.ShopCardItemState

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
	val backgroundImage = getShopCardBackgroundImage(state.product)
	
	Box(modifier = modifier
		.fillMaxWidth()
		.heightIn(max = 200.dp)
	) {
		ImageRouter(
			state = backgroundImage,
			modifier = Modifier.fillMaxWidth(),
			contentScale = ContentScale.Inside
		)
	}
}

// eventually there will be images from backend and use coil 
private fun getShopCardBackgroundImage(product: ProductContent?): ImageState {
	return when (product?.type?.lowercase()) {
		"rune" -> ImageState.Basic.RuneShopCard
		"glyph" -> ImageState.Basic.GlyphShopCard
		"deck" -> ImageState.Basic.CrateShopCard
		else -> ImageState.Basic.CrateShopCard // Default to crate if product type is unknown
	}
}
