package com.cypherose.common.components.buttons.presentation.adapters

import com.cypherose.business.interfaces.Mapper
import com.cypherose.common.components.buttons.presentation.RectangleButtonState

interface RectangleButtonMapper : Mapper<RectangleButtonMapper.Param, RectangleButtonState> {
	data class Param(
		val onClick: () -> Unit,
		val text: String,
	)
}

class RectangleButtonMapperImpl : RectangleButtonMapper {
	override fun map(param: RectangleButtonMapper.Param): RectangleButtonState {
		return RectangleButtonState.Content(
			onClick = param.onClick,
			text = param.text,
		)
	}
}
