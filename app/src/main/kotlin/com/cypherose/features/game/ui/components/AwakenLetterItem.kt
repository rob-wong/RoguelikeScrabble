package com.cypherose.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cypherose.business.models.Letter
import com.cypherose.common.components.presentation.DeckType
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.ui.LetterRouter

@Composable
fun AwakenLetterItem(
	letter: Letter,
	isSelected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val borderColor = if (isSelected) Color.White else Color.Transparent
	val borderWidth = if (isSelected) 4.dp else 0.dp
	
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.padding(horizontal = 4.dp, vertical = 4.dp)
			.background(
				color = Color.Transparent,
				shape = RoundedCornerShape(12.dp)
			)
			.border(
				width = borderWidth,
				color = borderColor,
				shape = RoundedCornerShape(12.dp)
			)
			.clickable(onClick = onClick)
			.padding(8.dp),
		contentAlignment = Alignment.Center
	) {
		LetterRouter(
			state = LetterState.Display(
				type = DeckType.Default,
				letter = letter.letter.toUpperCase(),
				level = letter.level
			)
		)
	}
}
