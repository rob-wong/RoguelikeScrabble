package com.cypherose.business.templateengine.data

import android.content.Context
import android.util.Log
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.interfaces.DataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateDataSource @Inject constructor(
	@ApplicationContext private val context: Context,
	private val dispatcherProvider: DispatcherProvider
) : DataSource {
	
	suspend fun fetchTemplateData(path: String): String {
		return withContext(dispatcherProvider.io) {
			loadTemplateData(path)
		}
	}

	private fun loadTemplateData(path: String): String {
		return try {
			val input = context.assets.open(path)
			input.bufferedReader().use { it.readText() }
		} catch (e: java.io.FileNotFoundException) {
			Log.e("TemplateDataSource", "$path not found", e)
			"{\"data\": []}"
		} catch (e: java.io.IOException) {
			Log.e("TemplateDataSource", "Failed to load $path", e)
			"{\"data\": []}"
		}
	}
}
