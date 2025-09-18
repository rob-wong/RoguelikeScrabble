package com.example.gymapprefactor.common.components.presentation.adapters

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState

interface RectangleButtonMapper : Mapper<RectangleButtonMapper.Param, RectangleButtonState> {
	data class Param(
		val onClick: () -> Unit,
		val text: String,
	)
}

class RectangleButtonMapperImpl : RectangleButtonMapper{
	override fun map(param: RectangleButtonMapper.Param): RectangleButtonState {
		return RectangleButtonState.Content(
			onClick = param.onClick,
			text = param.text,
		)
	}
}
