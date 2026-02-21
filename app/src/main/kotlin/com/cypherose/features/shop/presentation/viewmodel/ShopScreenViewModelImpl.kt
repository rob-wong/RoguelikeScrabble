package com.cypherose.features.shop.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.models.User
import com.cypherose.business.templateengine.domain.usecases.LoadShopScreenUseCase
import com.cypherose.business.user.domain.UserBusinessMediator
import com.cypherose.features.navigation.presentation.models.NavigationAction
import com.cypherose.features.navigation.presentation.state.NavigationReducer
import com.cypherose.features.shop.presentation.models.ShopScreenAction
import com.cypherose.features.shop.presentation.state.ShopScreenReducer
import com.cypherose.features.templateengine.presentation.services.TemplateContentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShopScreenViewModelImpl @Inject constructor(
	private val shopScreenReducer: ShopScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
	private val userBusinessMediator: UserBusinessMediator,
	private val loadShopScreenUseCase: LoadShopScreenUseCase,
	private val templateContentService: TemplateContentService,
): ShopScreenViewModel() {
	override val state = shopScreenReducer.state

	private companion object {
		const val TAG = "ShopScreenViewModel"
	}

	init {
		startObservingUserUpdates()
		loadInitialUserContent()
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
			userBusinessMediator.getUser().fold(
				onSuccess = { user ->
					updateContentWithUser(user)
				},
				onFailure = { error ->
					Timber.e(error, "Failed to load user for shop screen")
				}
			)
		}
	}

	private suspend fun updateContentWithUser(user: User) {
		val instances = loadShopScreenUseCase("shop_content.json")

		val templateStates = instances.fold(
			onSuccess = { instances ->
				templateContentService.extractStatesFromInstances(instances)
			},
			onFailure = { error ->
				Log.e(TAG, "Failed to load template instances", error)
				Result.failure(error)
			}
		)
		
		templateStates.fold(
			onSuccess = { templateStates ->
				templateStates.forEachIndexed { index, state ->
					Log.d(TAG, "  State[$index]: ${state::class.simpleName}, $state")
				}
			},
			onFailure = { error ->
				Log.e(TAG, "Failed to extract template states", error)
			}
		)

		shopScreenReducer.update(
			ShopScreenAction.SetContent(
				runesCount = user.runesCount,
				onBackPressed = ::onBackPressed,
				templateStates = templateStates.getOrElse { emptyList() }
			)
		)
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}
}
