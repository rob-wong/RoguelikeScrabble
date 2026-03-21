package com.cypherose.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.cypherose.R

val common = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFFFF8E7), // Off-white
	fontSize = 45.sp,
)

val appCommon = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixeloid_sans)),
	color = Color(0xFFFFF8E7), // Off-white (matches `common`)
	fontSize = 15.sp,
	shadow = Shadow(
		color = Color.Black,
		offset = Offset(1.5f, 1.5f),
		blurRadius = 1.5f
	),
)

// App UI fonts: same font/shadow/size as `appCommon`, only color changes.
val appUncommon = appCommon.copy(color = Color(0xFFA9EFA5)) // Soft green
val appRare = appCommon.copy(color = Color(0xFF5CC8FF)) // Bright sky blue
val appEpic = appCommon.copy(color = Color(0xFFC36CFF)) // Deep violet
val appLegendary = appCommon.copy(color = Color(0xFFFFC857)) // Gold-orange

// Chance multiplier base font (oscillating per-character color is applied in the composable).
val appChance = appCommon.copy()

val uncommon = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFA9EFA5), // Soft green
	fontSize = 45.sp,
)

val rare = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFF5CC8FF), // Bright sky blue
	fontSize = 45.sp,
)

val epic = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFC36CFF), // Deep violet
	fontSize = 45.sp,
)

val legendary = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFFFC857), // Gold-orange
	fontSize = 45.sp,
)

// Chance multiplier font - uses oscillating colors per character
// The color is applied per-character in the composable, not as a single TextStyle color
val chanceOscillatingColors = listOf(
	Color(0xFFFF5F5F), // Red
	Color(0xFF2A2A2A), // Dark gray/black
	Color(0xFF7DE77D)  // Green
)

val chance = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFFFF8E7), // Base color (off-white) - will be overridden per character
	fontSize = 45.sp,
)

// Chance multiplier text colors - green for positive, red for negative
val chanceMultiplierPositiveColor = Color(0xFF00FF00) // Green
val chanceMultiplierNegativeColor = Color(0xFFFF0000) // Red
