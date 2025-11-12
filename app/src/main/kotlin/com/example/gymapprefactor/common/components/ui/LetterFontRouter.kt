package com.example.gymapprefactor.common.components.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import com.example.gymapprefactor.ui.theme.common
import com.example.gymapprefactor.ui.theme.epic
import com.example.gymapprefactor.ui.theme.legendary
import com.example.gymapprefactor.ui.theme.rare
import com.example.gymapprefactor.ui.theme.uncommon

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

