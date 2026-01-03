package com.example.gymapprefactor.features.shop.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.User
import com.example.gymapprefactor.business.templateengine.domain.usecases.LoadShopScreenUseCase
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenAction
import com.example.gymapprefactor.features.shop.presentation.state.ShopScreenReducer
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic.BasicListViewModel
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.shopcard.ShopCardItemViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopScreenViewModelImpl @Inject constructor(
	private val shopScreenReducer: ShopScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
	private val userBusinessMediator: UserBusinessMediator,
	private val loadShopScreenUseCase: LoadShopScreenUseCase,
): ShopScreenViewModel() {
	override val state = shopScreenReducer.state

	init {
		startObservingUserUpdates()
		loadInitialUserContent()
		loadTemplateContent()
	}

	private fun startObservingUserUpdates() {
		viewModelScope.launch(dispatcherProvider.main) {
			userBusinessMediator.getUserFlow().collect { user ->
				handleUserUpdate(user)
			}
		}
	}

	private suspend fun handleUserUpdate(user: User?) {
		if (user != null) {
			updateContentWithUser(user)
		}
	}

	private fun loadInitialUserContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			val user = userBusinessMediator.getUser()
			updateContentWithUser(user)
		}
	}

	private suspend fun updateContentWithUser(user: User) {
		shopScreenReducer.update(
			ShopScreenAction.SetContent(
				runesCount = user.runesCount,
				onBackPressed = ::onBackPressed
			)
		)
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}

	// this is temporary, will implement a ContentRouter later
	private fun loadTemplateContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			val result = loadShopScreenUseCase()
			result.onSuccess { templateInstances ->
				println("=== Template Engine Results ===")
				println("Total instances: ${templateInstances.size}")
				templateInstances.forEach { instance ->
					println("\n--- Template Instance ---")
					println("ID: ${instance.id}")
					println("Template ID: ${instance.templateId}")
					println("Type: ${instance.type}")
					println("ViewModel: ${instance.viewModel::class.simpleName}")

					try {
						when (instance.viewModel) {
							is BasicListViewModel -> {
								val state = instance.viewModel.state.first()
								println("BasicList State: $state")
							}
							is ShopCardItemViewModel -> {
								val state = instance.viewModel.state.first()
								println("ShopCardItem State: $state")
							}
							else -> {
								println("Unknown ViewModel type: ${instance.viewModel::class.simpleName}")
							}
						}
					} catch (e: Exception) {
						println("Error accessing state: ${e.message}")
					}
				}
				println("=== End Template Engine Results ===\n")
			}.onFailure { error ->
				println("Error loading template content: ${error.message}")
				error.printStackTrace()
			}
		}
	}
}
