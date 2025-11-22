package com.example.gymapprefactor.features.shop.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
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
	private val dispatcherProvider: DispatcherProvider,
	private val userBusinessMediator: UserBusinessMediator,
): ShopScreenViewModel() {
	override val state = shopScreenReducer.state

	init {
		setContent()
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			val user = userBusinessMediator.getUser()
			shopScreenReducer.update(
				ShopScreenAction.SetContent(
					runesCount = user.runesCount,
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
