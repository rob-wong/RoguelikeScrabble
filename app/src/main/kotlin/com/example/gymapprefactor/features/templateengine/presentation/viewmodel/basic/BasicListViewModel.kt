package com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.templateengine.presentation.models.basic.BasicListState
import kotlinx.coroutines.flow.Flow

abstract class BasicListViewModel : ViewModel() {
	abstract val state: Flow<BasicListState>
	abstract suspend fun setContent(id: String, items: List<com.example.gymapprefactor.business.interfaces.State>)
}
