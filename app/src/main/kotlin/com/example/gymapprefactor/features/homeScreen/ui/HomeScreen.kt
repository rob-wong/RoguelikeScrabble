package com.example.gymapprefactor.features.homeScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
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
        is HomeScreenState.Content -> HomeScreenLayout(state, modifier)
        is HomeScreenState.None -> Unit
    }
}

@Composable
private fun HomeScreenLayout(
    state: HomeScreenState.Content,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        HomeScreenButtonColumn(state, Modifier)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ResourceBarRouter(state.resourceBar)
            HomeScreenContent(state, Modifier)
        }
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeScreenState.Content,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = homeScreenColumnPaddingRouter()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ButtonRouter(state.playButton)
    }
}

@Composable
private fun BoxScope.HomeScreenButtonColumn(
    state: HomeScreenState.Content,
    modifier: Modifier = Modifier,
) {
   Column(modifier
       .padding(top = 10.dp, end = 10.dp)
       .align(Alignment.TopEnd)
   ) {
       ButtonRouter(state.shopButton, Modifier.size(40.dp))
       Spacer(Modifier.height(10.dp))
       ButtonRouter(state.upgradeButton, Modifier.size(40.dp))
   }
}

private fun homeScreenColumnPaddingRouter(): Dp {
    return when(DeviceUtil.isLandscape) {
        true -> DeviceUtil.getColumnWidthDp(1)
        false -> SpacerUtil.spacer_20
    }
}

@DevicePreviews
@Composable
internal fun HomeScreenPreview(
    @PreviewParameter(HomeScreenProvider::class) state: HomeScreenState.Content
) {
    HomeScreenLayout(state)
}
