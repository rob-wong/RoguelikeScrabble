package com.example.gymapprefactor.common.components.presentation

import com.example.gymapprefactor.business.interfaces.State

sealed class SelectorState : State {
    data class Content(val options: List<Int>): SelectorState()

    data object None : SelectorState()
}
