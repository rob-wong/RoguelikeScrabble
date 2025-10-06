package com.example.gymapprefactor.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.gymapprefactor.R

val common = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFFFF8E7), // Off-white
	fontSize = 300.sp,
)

val uncommon = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFA9EFA5), // Soft green
	fontSize = 300.sp,
)

val rare = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFF5CC8FF), // Bright sky blue
	fontSize = 300.sp,
)

val epic = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFC36CFF), // Deep violet
	fontSize = 300.sp,
)

val legendary = TextStyle(
	fontFamily = FontFamily(Font(R.font.pixel_operator)),
	color = Color(0xFFFFC857), // Gold-orange
	fontSize = 300.sp,
)