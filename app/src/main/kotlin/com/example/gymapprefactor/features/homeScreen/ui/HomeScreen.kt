package com.example.gymapprefactor.features.homeScreen.ui

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
import com.example.gymapprefactor.common.components.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.TopBarRouter
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
import com.example.gymapprefactor.features.homeScreen.presentation.viewmodel.HomeScreenViewModelImpl
import com.example.gymapprefactor.features.homeScreen.ui.preview.HomeScreenProvider

@Composable
fun HomeScreenRouter(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModelImpl = hiltViewModel(),
) {
    val screenState: HomeScreenState by viewModel.state.collectAsStateWithLifecycle(
        HomeScreenState.None
    )

    when(val state = screenState) {
        is HomeScreenState.Content -> HomeScreenContent(state, modifier)
        is HomeScreenState.None -> Unit
    }
}

@Composable
fun HomeScreenContent(
    state: HomeScreenState.Content,
    modifier: Modifier = Modifier,
) {
    TopBarRouter(state = state.topBarState)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = homeScreenColumnPaddingRouter()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ButtonRouter(state.onRoutines, Modifier)

        Spacer(modifier = Modifier.height(SpacerUtil.spacer_10))

        //FoldingButtonTest()

        //Spacer(modifier = Modifier.height(SpacerUtil.spacer_10))

        ButtonRouter(state.onExercises)

        Spacer(modifier = Modifier.height(SpacerUtil.spacer_10))

        ButtonRouter(state.onViewStats)
    }
}

private fun homeScreenColumnPaddingRouter(): Dp {
    return when(DeviceUtil.isLandscape) {
        true -> DeviceUtil.getColumnWidthDp(1)
        false -> SpacerUtil.spacer_20
    }
}

@Preview
@Composable
internal fun HomeScreenPreview(
    @PreviewParameter(HomeScreenProvider::class) state: HomeScreenState.Content
) {
    HomeScreenContent(state)
}
