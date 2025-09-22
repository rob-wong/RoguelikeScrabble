package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState

interface UpgradeScreenReducer : Reducer<UpgradeScreenState, UpgradeScreenAction>
