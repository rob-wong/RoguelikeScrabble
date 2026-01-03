package com.example.gymapprefactor.business.templateengine.domain.registry

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.data.models.ListNet

interface ListTemplateFactory {
	fun createViewModel(listNet: ListNet): ViewModel
}
