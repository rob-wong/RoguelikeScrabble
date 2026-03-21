package com.cypherose.common.components.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import com.cypherose.R
import com.cypherose.app.util.fitAspect
import com.cypherose.common.components.presentation.ImageState

@Composable
@SuppressWarnings("CyclomaticComplexMethod")
fun ImageRouter(
	state: ImageState,
	modifier: Modifier = Modifier,
	isLandscape: Boolean = false,
	contentScale: ContentScale = ContentScale.Crop,
	heightMultiplier: Float? = null,
	heightDp: Dp? = null,
) {
	when (state) {
		is ImageState.NinePatch -> NinePatchImageRouter(
			state = state,
			modifier = modifier,
			contentScale = contentScale,
			heightMultiplier = heightMultiplier,
			heightDp = heightDp
		)
		is ImageState.Basic -> BasicImageRouter(state, modifier, isLandscape, contentScale)
		is ImageState.None -> return
	}
}

@Composable
@Suppress("UnusedParameter")
private fun NinePatchImageRouter(
	state: ImageState.NinePatch,
	modifier: Modifier = Modifier,
	contentScale: ContentScale = ContentScale.FillWidth,
	heightMultiplier: Float? = null,
	heightDp: Dp? = null,
) {
	val drawableRes = when (state) {
		is ImageState.NinePatch.ResourceBarBackground -> R.drawable.resource_bar_v2
		is ImageState.NinePatch.DialogBackground -> R.drawable.image_dialog_background
		is ImageState.NinePatch.EffectBackgroundCommon -> R.drawable.effect_background_common
	}
	
	NinePatchImageRouter(
		drawableRes = drawableRes,
		modifier = modifier,
		heightMultiplier = heightMultiplier,
		heightDp = heightDp
	)
}

@Composable
@SuppressWarnings("CyclomaticComplexMethod")
private fun BasicImageRouter(
	state: ImageState.Basic,
	modifier: Modifier = Modifier,
	isLandscape: Boolean = false,
	contentScale: ContentScale = ContentScale.Crop,
) {
	val painter = when (state) {
		is ImageState.Basic.RectangularButtonBackground -> painterResource(R.drawable.background_rectangular_button)
		is ImageState.Basic.SettingsButton -> painterResource(R.drawable.settings_gear)
		is ImageState.Basic.UpgradeButton -> painterResource(R.drawable.icon_upgrade_button)
		is ImageState.Basic.DialogBackground -> if (isLandscape) {
			painterResource(R.drawable.dialog_background_landscape)
		} else {
			painterResource(R.drawable.dialog_background_portrait)
		}
		is ImageState.Basic.PlayTextIcon -> painterResource(R.drawable.icon_play_text)
		is ImageState.Basic.BackIcon -> painterResource(R.drawable.icon_back)
		is ImageState.Basic.QuitIcon -> painterResource(R.drawable.icon_quit)
		is ImageState.Basic.ConfirmIcon -> painterResource(R.drawable.icon_confirm)
		is ImageState.Basic.DismissIcon -> painterResource(R.drawable.icon_dismiss)
		is ImageState.Basic.DiscardIcon -> painterResource(R.drawable.icon_discard)
		is ImageState.Basic.ShopIcon -> painterResource(R.drawable.icon_shop)
		is ImageState.Basic.UpgradeIcon -> painterResource(R.drawable.icon_upgrade)
		is ImageState.Basic.GlyphIcon -> painterResource(R.drawable.icon_glyph)
		is ImageState.Basic.RuneIcon -> painterResource(R.drawable.icon_rune)

		is ImageState.Basic.BasicBagIcon -> painterResource(R.drawable.icon_basic_bag)
		is ImageState.Basic.PlaysLeftIcon -> painterResource(R.drawable.icon_plays_left)
		is ImageState.Basic.DiscardsLeftIcon -> painterResource(R.drawable.icon_discards_left)
		is ImageState.Basic.GameOverText -> painterResource(R.drawable.icon_game_over_text)

		is ImageState.Basic.DefaultLetterBackground -> painterResource(R.drawable.icon_letter_background_default)
		is ImageState.Basic.RoseEffectBackdrop -> painterResource(R.drawable.image_rose_effect_backdrop)

		is ImageState.Basic.AwakenMidshopCard -> painterResource(R.drawable.image_awaken_midshop_card)
		is ImageState.Basic.ExpungeMidshopCard -> painterResource(R.drawable.image_expunge_midshop_card)
		is ImageState.Basic.PerfectionismMidshopCard -> painterResource(R.drawable.image_perfectionism_midshop_card)
		is ImageState.Basic.PersistenceMidshopCard -> painterResource(R.drawable.image_persistence_midshop_card)
		is ImageState.Basic.UpgradeMidshopCard -> painterResource(R.drawable.image_upgrade_midshop_card)

		is ImageState.Basic.RuneShopCard -> painterResource(R.drawable.image_rune_shopcard)
		is ImageState.Basic.GlyphShopCard -> painterResource(R.drawable.image_glyph_shopcard)
		is ImageState.Basic.CrateShopCard -> painterResource(R.drawable.image_crate_shopcard)

		is ImageState.Basic.ComingSoon -> painterResource(R.drawable.image_comingsoon)
	}
	
	Image(
		painter = painter,
		contentDescription = null,
		modifier = modifier.fitAspect(painter),
		alignment = Alignment.Center,
		contentScale = contentScale
	)
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun NinePatchImageRouter(
	drawableRes: Int,
	modifier: Modifier = Modifier,
	heightMultiplier: Float? = null,
	heightDp: Dp? = null,
) {
	val context = LocalContext.current
	val density = LocalDensity.current
	val drawable = remember { ContextCompat.getDrawable(context, drawableRes) }

	val intrinsicHeightPx = remember(drawable) {
		drawable?.intrinsicHeight ?: 0
	}

	val intrinsicHeightDp = remember(intrinsicHeightPx, density) {
		with(density) { (intrinsicHeightPx / density.density).toDp() }
	}

	val displayHeightDp = remember(intrinsicHeightDp, heightMultiplier, heightDp, density) {
		calculateDisplayHeight(intrinsicHeightDp, heightMultiplier, heightDp, density)
	}
	
	BoxWithConstraints(
		modifier = modifier
			.fillMaxWidth()
			.height(displayHeightDp)
	) {
		val widthPx = with(density) { maxWidth.toPx().toInt() }
		val heightPx = heightDp?.let { explicitHeightDp ->
			with(density) { explicitHeightDp.toPx().toInt() }
		} ?: calculateHeightPx(intrinsicHeightPx, heightMultiplier)

		val effectiveHeightMultiplier = if (heightDp != null) null else heightMultiplier
		
		drawable?.let { d ->
			val bitmap = createNinePatchBitmap(
				drawable = d,
				widthPx = widthPx,
				heightPx = heightPx,
				heightMultiplier = effectiveHeightMultiplier,
				intrinsicHeightPx = intrinsicHeightPx,
			)
			
			bitmap?.let {
				Image(
					bitmap = it.asImageBitmap(),
					contentDescription = null,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.FillBounds
				)
			}
		}
	}
}

private fun calculateDisplayHeight(
	intrinsicHeightDp: Dp,
	heightMultiplier: Float?,
	heightDp: Dp?,
	density: androidx.compose.ui.unit.Density
): Dp {
	return heightDp ?: if (heightMultiplier != null) {
		with(density) { (intrinsicHeightDp.toPx() * heightMultiplier).toDp() }
	} else {
		intrinsicHeightDp
	}
}

private fun calculateHeightPx(intrinsicHeightPx: Int, heightMultiplier: Float?): Int {
	return if (heightMultiplier != null) {
		(intrinsicHeightPx * heightMultiplier).toInt()
	} else {
		intrinsicHeightPx
	}
}

private fun createNinePatchBitmap(
	drawable: android.graphics.drawable.Drawable,
	widthPx: Int,
	heightPx: Int,
	heightMultiplier: Float?,
	intrinsicHeightPx: Int
): Bitmap? {
	if (widthPx <= 0 || heightPx <= 0) {
		return null
	}
	
	return if (heightMultiplier != null && heightMultiplier != 1f) {
		createScaledBitmap(drawable, widthPx, heightPx, heightMultiplier, intrinsicHeightPx)
	} else {
		drawable.setBounds(0, 0, widthPx, heightPx)
		drawable.toBitmap(widthPx, heightPx)
	}
}

private fun createScaledBitmap(
	drawable: android.graphics.drawable.Drawable,
	widthPx: Int,
	heightPx: Int,
	heightMultiplier: Float,
	intrinsicHeightPx: Int
): Bitmap {
	val scaledBitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
	val canvas = Canvas(scaledBitmap)
	
	canvas.save()
	canvas.scale(1f, heightMultiplier)
	
	drawable.setBounds(0, 0, widthPx, intrinsicHeightPx)
	drawable.draw(canvas)
	canvas.restore()
	
	return scaledBitmap
}

@Composable
@Preview
private fun ImageRouterPreview() {
	ImageRouter(ImageState.Basic.DialogBackground, Modifier, false, ContentScale.FillWidth)
}
