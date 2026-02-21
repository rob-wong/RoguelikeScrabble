package com.cypherose.features.templateengine.presentation.viewmodel.basic

import androidx.lifecycle.ViewModel
import com.cypherose.features.templateengine.presentation.models.basic.BasicListState
import kotlinx.coroutines.flow.Flow

abstract class BasicListViewModel : ViewModel() {
	abstract val state: Flow<BasicListState>
	abstract suspend fun setContent(id: String, items: List<com.cypherose.business.interfaces.State>)
}
