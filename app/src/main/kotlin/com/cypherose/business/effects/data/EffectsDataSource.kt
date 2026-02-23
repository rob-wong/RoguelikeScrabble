package com.cypherose.business.effects.data

import android.content.Context
import android.util.Log
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectsDataSource @Inject constructor(
	@ApplicationContext private val context: Context,
	private val dispatcherProvider: DispatcherProvider,
	private val json: Json
) {
	private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
	private val _effectDescriptors = MutableSharedFlow<Map<String, EffectDescriptor>>(
		replay = 1,
		extraBufferCapacity = 0
	)
	
	@Volatile
	private var cachedDescriptors: Map<String, EffectDescriptor>? = null

	init {
		loadAndCacheDescriptors()
	}

	fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>> {
		return _effectDescriptors
	}

	private fun loadAndCacheDescriptors() {
		scope.launch {
			if (cachedDescriptors != null) return@launch
			val descriptorMap = withContext(dispatcherProvider.io) {
				loadEffectDescriptors()
			}
			cachedDescriptors = descriptorMap
			_effectDescriptors.emit(descriptorMap)
		}
	}

	private fun loadEffectDescriptors(): Map<String, EffectDescriptor> {
		return try {
			val input = context.assets.open("effect_descriptors.json")
			val jsonString = input.bufferedReader().use { it.readText() }
			val jsonObject = json.parseToJsonElement(jsonString).jsonObject
			
			val descriptorMap = mutableMapOf<String, EffectDescriptor>()

			jsonObject.forEach { (effectType, typeGroupValue) ->
				val typeGroup = typeGroupValue.jsonObject

				typeGroup.forEach { (effectName, configValue) ->
					val config = configValue.jsonObject
					descriptorMap[effectName] = EffectDescriptor(
						type = effectType,
						config = config
					)
				}
			}
			
			descriptorMap
		} catch (e: java.io.FileNotFoundException) {
			Log.e("EffectsDataSource", "effect_descriptors.json not found", e)
			emptyMap()
		} catch (e: java.io.IOException) {
			Log.e("EffectsDataSource", "Failed to load effect_descriptors.json", e)
			emptyMap()
		} catch (e: kotlinx.serialization.SerializationException) {
			Log.e("EffectsDataSource", "Failed to parse effect_descriptors.json", e)
			emptyMap()
		}
	}
}
