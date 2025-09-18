package com.example.gymapprefactor.features.routines.base.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.ui.RectangleButtonRouter
import com.example.gymapprefactor.common.components.ui.TopBarRouter
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState
import com.example.gymapprefactor.features.routines.base.presentation.viewmodel.BaseRoutineScreenViewModelImpl
import com.example.gymapprefactor.features.routines.base.ui.preview.BaseRoutineScreenProvider

@Composable
fun BaseRoutineScreenRouter(
    modifier: Modifier = Modifier,
    viewModel: BaseRoutineScreenViewModelImpl = hiltViewModel()
) {
    val screenState: BaseRoutineScreenState by viewModel.state.collectAsStateWithLifecycle(
        BaseRoutineScreenState.None
    )

    when (val state = screenState) {
        is BaseRoutineScreenState.Content -> BaseRoutineScreenContent(
            state = state,
            modifier = modifier,
        )
        is BaseRoutineScreenState.None -> Unit
    }
}

@Composable
fun BaseRoutineScreenContent(
    state: BaseRoutineScreenState.Content,
    modifier: Modifier = Modifier,
) {
    TopBarRouter(state = state.topBarState)

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = routinesColumnPaddingRouter()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.routineButtons.forEach {
            RectangleButtonRouter(it)
            Spacer(modifier = Modifier.height(SpacerUtil.spacer_18))
        }
        RectangleButtonRouter(state = state.createRoutineButton)
    }
}

private fun routinesColumnPaddingRouter(): Dp {
    return when(DeviceUtil.isLandscape) {
        true -> DeviceUtil.getColumnWidthDp(1)
        false -> SpacerUtil.spacer_20
    }
}

@Preview
@Composable
internal fun BaseRoutineScreenPreview(
    @PreviewParameter(BaseRoutineScreenProvider::class) state : BaseRoutineScreenState.Content
) {
    BaseRoutineScreenContent(state)
}
