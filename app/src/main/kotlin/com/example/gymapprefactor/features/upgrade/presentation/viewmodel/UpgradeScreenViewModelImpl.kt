package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpgradeScreenViewModelImpl @Inject constructor(
	private val upgradeScreenReducer: UpgradeScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
): UpgradeScreenViewModel() {
	override val state = upgradeScreenReducer.state

	init {
		setContent()
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			upgradeScreenReducer.update(
				UpgradeScreenAction.SetContent(
					runesCount = 10,
					onBackPressed = ::onBackPressed
				)
			)
		}
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}
}
