package com.example.gymapprefactor.business.templateengine.data

import android.content.Context
import android.util.Log
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.interfaces.DataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateDataSource @Inject constructor(
	@ApplicationContext private val context: Context,
	private val dispatcherProvider: DispatcherProvider
) : DataSource {
	
	suspend fun fetchTemplateData(): String {
		return withContext(dispatcherProvider.io) {
			loadTemplateData()
		}
	}

	private fun loadTemplateData(): String {
		return try {
			val input = context.assets.open("shop_content.json")
			input.bufferedReader().use { it.readText() }
		} catch (e: java.io.FileNotFoundException) {
			Log.e("TemplateDataSource", "shop_content.json not found", e)
			"{\"data\": []}"
		} catch (e: java.io.IOException) {
			Log.e("TemplateDataSource", "Failed to load shop_content.json", e)
			"{\"data\": []}"
		}
	}
}
