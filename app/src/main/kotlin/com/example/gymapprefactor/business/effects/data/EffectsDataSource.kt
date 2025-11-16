package com.example.gymapprefactor.business.effects.data

import android.content.Context
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectsDataSource @Inject constructor(
	@ApplicationContext private val context: Context,
	private val dispatcherProvider: DispatcherProvider
) {
	private val json = Json { ignoreUnknownKeys = true }

	fun getEffects(): Flow<Map<String, Int>> = flow {
		val effectMap = withContext(dispatcherProvider.io) {
			loadEffectMap()
		}
		emit(effectMap)
	}

	fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>> = flow {
		val descriptorMap = withContext(dispatcherProvider.io) {
			loadEffectDescriptors()
		}
		emit(descriptorMap)
	}

	// This will eventually be a backend call once we have one
	private fun loadEffectMap(): Map<String, Int> {
		return try {
			val input = context.assets.open("effect_map.json")
			val jsonString = input.bufferedReader().use { it.readText() }
			json.decodeFromString<Map<String, Int>>(jsonString)
		} catch (e: java.io.IOException) {
			android.util.Log.e("EffectsDataSource", "Failed to load effect_map.json", e)
			emptyMap()
		} catch (e: kotlinx.serialization.SerializationException) {
			android.util.Log.e("EffectsDataSource", "Failed to parse effect_map.json", e)
			emptyMap()
		}
	}

	private fun loadEffectDescriptors(): Map<String, EffectDescriptor> {
		return try {
			// Try to load new format first
			val input = context.assets.open("effect_descriptors.json")
			val jsonString = input.bufferedReader().use { it.readText() }
			val jsonObject = json.parseToJsonElement(jsonString).jsonObject
			
			return jsonObject.mapValues { (_, value) ->
				val descriptorObj = value.jsonObject
				EffectDescriptor(
					type = descriptorObj["type"]?.jsonPrimitive?.content ?: "fixed_addition",
					config = descriptorObj["config"]?.jsonObject ?: JsonObject(emptyMap())
				)
			}
		} catch (e: java.io.FileNotFoundException) {
			// Fallback to old format and convert
			android.util.Log.d("EffectsDataSource", "effect_descriptors.json not found, converting from effect_map.json", e)
			convertOldFormatToDescriptors()
		} catch (e: java.io.IOException) {
			android.util.Log.e("EffectsDataSource", "Failed to load effect_descriptors.json", e)
			convertOldFormatToDescriptors()
		} catch (e: kotlinx.serialization.SerializationException) {
			android.util.Log.e("EffectsDataSource", "Failed to parse effect_descriptors.json", e)
			convertOldFormatToDescriptors()
		}
	}

	private fun convertOldFormatToDescriptors(): Map<String, EffectDescriptor> {
		return try {
			val oldMap = loadEffectMap()
			oldMap.mapValues { (_, value) ->
				EffectDescriptor(
					type = "fixed_addition",
					config = json.parseToJsonElement("""{"value": $value}""").jsonObject
				)
			}
		} catch (e: Exception) {
			android.util.Log.e("EffectsDataSource", "Failed to convert old format", e)
			emptyMap()
		}
	}
}
