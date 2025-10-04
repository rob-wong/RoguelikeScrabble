package com.example.gymapprefactor.business.startup

import android.content.Context
import com.example.gymapprefactor.app.util.DeviceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartupController @Inject constructor(
	@ApplicationContext private val context: Context,
) {
	fun startup() {
		DeviceUtil.init(context)
	}
}
