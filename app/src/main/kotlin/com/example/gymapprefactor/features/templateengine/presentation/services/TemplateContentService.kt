package com.example.gymapprefactor.features.templateengine.presentation.services

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.business.templateengine.domain.TemplateInstance
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateItem
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateList
import com.example.gymapprefactor.features.templateengine.presentation.registry.TemplateRegistry
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic.BasicListViewModel
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.shopcard.ShopCardItemViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface TemplateContentService {
	suspend fun extractStatesFromInstances(instances: List<TemplateInstance>): Result<List<State>>
}

class TemplateContentServiceImpl @Inject constructor(
	private val templateRegistry: TemplateRegistry
) : TemplateContentService {

	override suspend fun extractStatesFromInstances(instances: List<TemplateInstance>): Result<List<State>> {
		return try {
			val states = extractStatesFromInstancesInternal(instances)
			Result.success(states)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	private suspend fun extractStatesFromInstancesInternal(instances: List<TemplateInstance>): List<State> {
		// Build a set of nested item IDs to identify top-level instances
		val nestedIds = mutableSetOf<String>()
		instances.forEach { instance ->
			if (instance.content is TemplateList) {
				collectNestedIds(instance.content, nestedIds)
			}
		}
		
		// Only process top-level instances (those not in nestedIds)
		return instances
			.filter { it.id !in nestedIds }
			.map { instance ->
				extractStateFromInstance(instance)
			}
	}

	private fun collectNestedIds(templateList: TemplateList, nestedIds: MutableSet<String>) {
		templateList.items.forEach { itemContent ->
			nestedIds.add(
				when (itemContent) {
					is TemplateList -> itemContent.id
					is TemplateItem -> itemContent.id
				}
			)
			if (itemContent is TemplateList) {
				collectNestedIds(itemContent, nestedIds)
			}
		}
	}

	private suspend fun extractStateFromInstance(instance: TemplateInstance): State {
		return when (val content = instance.content) {
			is TemplateList -> extractStateFromList(instance, content)
			is TemplateItem -> extractStateFromItem(instance, content)
		}
	}

	private suspend fun extractStateFromList(
		instance: TemplateInstance,
		templateList: TemplateList
	): State {
		val factory = templateRegistry.getListFactory(instance.templateId)
			?: throw IllegalArgumentException("No factory found for list template_id: ${instance.templateId}")
		
		val viewModel = factory.createViewModel(templateList)
		
		// Extract states from nested items recursively
		val nestedStates = templateList.items.map { itemContent ->
			when (itemContent) {
				is TemplateList -> {
					val nestedInstance = TemplateInstance(
						id = itemContent.id,
						templateId = itemContent.templateId,
						type = "list",
						content = itemContent
					)
					extractStateFromInstance(nestedInstance)
				}
				is TemplateItem -> {
					val nestedInstance = TemplateInstance(
						id = itemContent.id,
						templateId = itemContent.templateId,
						type = "item",
						content = itemContent
					)
					extractStateFromInstance(nestedInstance)
				}
			}
		}
		
		// Set content on the ViewModel with nested states
		if (viewModel is BasicListViewModel) {
			viewModel.setContent(instance.id, nestedStates)
			return viewModel.state.first()
		}
		
		throw IllegalArgumentException("ViewModel is not a BasicListViewModel")
	}

	private suspend fun extractStateFromItem(
		instance: TemplateInstance,
		templateItem: TemplateItem
	): State {
		val factory = templateRegistry.getItemFactory(instance.templateId)
			?: throw IllegalArgumentException("No factory found for item template_id: ${instance.templateId}")
		
		val viewModel = factory.createViewModel(templateItem)
		
		if (viewModel is ShopCardItemViewModel) {
			return viewModel.state.first()
		}
		
		throw IllegalArgumentException("ViewModel is not a ShopCardItemViewModel")
	}
}
