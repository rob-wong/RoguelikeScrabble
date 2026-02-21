package com.cypherose.common.components.presentation

import com.cypherose.business.interfaces.State

sealed class SelectorState : State {
    data class Content(val options: List<Int>): SelectorState()

    data object None : SelectorState()
}
