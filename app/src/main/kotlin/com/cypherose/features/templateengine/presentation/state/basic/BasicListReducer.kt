package com.cypherose.features.templateengine.presentation.state.basic

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.templateengine.presentation.models.basic.BasicListAction
import com.cypherose.features.templateengine.presentation.models.basic.BasicListState

interface BasicListReducer : Reducer<BasicListState, BasicListAction>
