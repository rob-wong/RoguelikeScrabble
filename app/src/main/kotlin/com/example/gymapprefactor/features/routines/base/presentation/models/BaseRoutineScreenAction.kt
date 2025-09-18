package com.example.gymapprefactor.features.routines.base.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class BaseRoutineScreenAction : Action {

    data class SetContent(
        val routineButtons: List<RectangleButtonState>,
        val createRoutineButton: RectangleButtonState,
        val topBarState: TopBarState,
    ) : BaseRoutineScreenAction()

    data object None : BaseRoutineScreenAction()
}

/*
* OKAYYY THIS IS THE PLAN MOVING FORWARD
* ADD A LIST OF MAYBE EXERCISES OR ONCLICKS? MAYBE A STATE FOR THIS IDK
* SEND IT TO THE UI LAYER AND MAKE BUTTONS FOR EACH
* PUT IT IN A SCROLLVIEW MAYBE
* USE THAT TO TEST THE APP DATA MODEL
* ONCE THERE ARE BUTTONS FOR EACH AND ITS WORKING ALRIGHT
* ADD A CREATE PAGE AND NAVIGATION
* MAYYYYBE MAKE A ROUTINES HOST CALLING UI ROUTERS INSTEAD OF ROUTINES NAVIGATION PAGES
*
*
* THEN ADD BACK BUTTON
* */
