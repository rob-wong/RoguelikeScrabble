package com.cypherose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.cypherose.app.util.SpacerUtil
import com.cypherose.business.models.AppDataModel
import com.cypherose.business.startup.StartupController
import com.cypherose.common.components.ui.AppBackground
import com.cypherose.common.components.ui.ScreenBackgroundRouter
import com.cypherose.features.dialogs.ui.DialogRoot
import com.cypherose.features.navigation.ui.NavigationHost
import com.cypherose.features.settings.ui.SettingsRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var startupController: StartupController

    @Inject lateinit var appDataModel: AppDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            appDataModel.fetchOrCreateUser().fold(
                onSuccess = {
                    startupController.startup()
                },
                onFailure = { error ->
                    Timber.e(error, "Failed to fetch or create user")
                }
            )
        }

        setContent {
            AppBackground {
                LaunchAppContent()
            }
        }
    }
}

@Composable
private fun LaunchAppContent() {
    Surface(
        modifier = Modifier,
        color = Color.Transparent,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ScreenBackgroundRouter(
                modifier = Modifier.fillMaxSize()
            )

            NavigationHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = SpacerUtil.screenVerticalPadding)
            )
            
            SettingsRoot(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = SpacerUtil.screenVerticalPadding)
            )
            DialogRoot()
        }
    }
}
