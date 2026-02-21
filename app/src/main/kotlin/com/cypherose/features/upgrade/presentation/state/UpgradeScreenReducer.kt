package com.cypherose.features.upgrade.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.upgrade.presentation.models.UpgradeScreenAction
import com.cypherose.features.upgrade.presentation.models.UpgradeScreenState

interface UpgradeScreenReducer : Reducer<UpgradeScreenState, UpgradeScreenAction>
