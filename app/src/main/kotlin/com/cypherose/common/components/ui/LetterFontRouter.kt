package com.cypherose.common.components.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import com.cypherose.ui.theme.common
import com.cypherose.ui.theme.epic
import com.cypherose.ui.theme.legendary
import com.cypherose.ui.theme.rare
import com.cypherose.ui.theme.uncommon

@Composable
@ReadOnlyComposable
fun letterFontRouter(level: Int): TextStyle {
	return when (level) {
		1 -> common
		2 -> uncommon
		3 -> rare
		4 -> epic
		5 -> legendary
		else -> common
	}
}

