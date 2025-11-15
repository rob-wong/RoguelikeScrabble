package com.example.gymapprefactor.business.effects.data

import android.content.Context
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectsDataSource @Inject constructor(
	@ApplicationContext private val context: Context,
	private val dispatcherProvider: DispatcherProvider
) {
	fun getEffects(): Flow<Map<String, Int>> = flow {
		val effectMap = withContext(dispatcherProvider.io) {
			loadEffectMap()
		}
		emit(effectMap)
	}

	// This will eventually be a backend call once we have one
	private suspend fun loadEffectMap(): Map<String, Int> {
		return try {
			val input = context.assets.open("effect_map.json")
			val jsonString = input.bufferedReader().use { it.readText() }
			Json.decodeFromString<Map<String, Int>>(jsonString)
		} catch (e: java.io.IOException) {
			android.util.Log.e("EffectsDataSource", "Failed to load effect_map.json", e)
			emptyMap()
		} catch (e: kotlinx.serialization.SerializationException) {
			android.util.Log.e("EffectsDataSource", "Failed to parse effect_map.json", e)
			emptyMap()
		}
	}
}
