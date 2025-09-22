package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapprefactor.R
import com.example.gymapprefactor.app.util.fitAspect
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ImageState.*

@Composable
fun ImageRouter(
	state: ImageState,
	modifier: Modifier = Modifier,
	isLandscape: Boolean = false,
	contentScale: ContentScale = ContentScale.Crop,
) {
	val painter = when (state) {
		ResourceBarBackground -> painterResource(R.drawable.resource_bar)
		SettingsButton -> painterResource(R.drawable.settings_gear)
		DialogBackground -> if (isLandscape) {
			painterResource(R.drawable.dialog_background_landscape)
		} else {
			painterResource(R.drawable.dialog_background_portrait)
		}
		BackIcon -> painterResource(R.drawable.icon_back)
		ShopIcon -> painterResource(R.drawable.icon_shop)
		UpgradeIcon -> painterResource(R.drawable.icon_upgrade)
		GlyphIcon -> painterResource(R.drawable.icon_glyph)
		RuneIcon -> painterResource(R.drawable.icon_rune)
		None -> return
	}
	Image(
		painter = painter,
		contentDescription = null,
		modifier = modifier
			.fitAspect(painter),
		alignment = Alignment.Center,
		contentScale = contentScale
	)
}

@Composable
@Preview
private fun ImageRouterPreview() {
	ImageRouter(DialogBackground, Modifier, false, ContentScale.FillWidth)
}
