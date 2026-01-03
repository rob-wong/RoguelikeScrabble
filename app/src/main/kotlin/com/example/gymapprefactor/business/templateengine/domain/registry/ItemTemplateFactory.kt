package com.example.gymapprefactor.business.templateengine.domain.registry

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.data.models.ItemNet

interface ItemTemplateFactory {
	fun createViewModel(itemNet: ItemNet): ViewModel
}
