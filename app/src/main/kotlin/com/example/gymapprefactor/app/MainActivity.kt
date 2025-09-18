package com.example.gymapprefactor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.common.components.ui.AppBackground
import com.example.gymapprefactor.features.navigation.ui.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeviceUtil.init(this)
        AppDataModel.initData()

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
    }
}
