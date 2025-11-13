package com.example.gymapprefactor.features.shop.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.presentation.models.BackgroundAction
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenAction
import com.example.gymapprefactor.features.shop.presentation.state.ShopScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopScreenViewModelImpl @Inject constructor(
	private val shopScreenReducer: ShopScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val backgroundReducer: BackgroundReducer,
	private val dispatcherProvider: DispatcherProvider
): ShopScreenViewModel() {
	override val state = shopScreenReducer.state

	init {
		setBackground()
		setContent()
	}

	private fun setBackground() {
		viewModelScope.launch(dispatcherProvider.main) {
			backgroundReducer.update(BackgroundAction.SetBackground(ScreenBackgroundState.Shop))
		}
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			shopScreenReducer.update(
				ShopScreenAction.SetContent(
					runesCount = 20,
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
