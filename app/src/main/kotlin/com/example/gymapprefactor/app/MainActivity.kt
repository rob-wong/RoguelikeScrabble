package com.example.gymapprefactor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.network.UserStorage
import com.example.gymapprefactor.business.startup.StartupController
import com.example.gymapprefactor.common.components.ui.AppBackground
import com.example.gymapprefactor.features.dialogs.ui.DialogRoot
import com.example.gymapprefactor.features.navigation.ui.NavigationHost
import com.example.gymapprefactor.features.settings.ui.SettingsRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var startupController: StartupController

    @Inject lateinit var appDataModel: AppDataModel
    @Inject lateinit var tempUserStorage: UserStorage // testing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            appDataModel.fetchOrCreateUser()
            startupController.startup().also { // testing
                println(tempUserStorage.loadUser()?.username)
            }
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
        NavigationHost()
        
        SettingsRoot()
        DialogRoot()
    }
}
