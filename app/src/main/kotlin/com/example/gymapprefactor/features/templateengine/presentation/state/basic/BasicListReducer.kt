package com.example.gymapprefactor.features.templateengine.presentation.state.basic

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.templateengine.presentation.models.basic.BasicListAction
import com.example.gymapprefactor.features.templateengine.presentation.models.basic.BasicListState

interface BasicListReducer : Reducer<BasicListState, BasicListAction>
