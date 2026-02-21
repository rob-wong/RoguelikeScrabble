package com.cypherose.app

import android.app.Application
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RoguelikeApp : Application() {
	override fun onCreate() {
		super.onCreate()
		if (isDebuggable()) {
			Timber.plant(Timber.DebugTree())
		} else {
			Timber.plant(object : Timber.Tree() {
				override fun log(priority: Int, tag: String?, message: String, t: Throwable?) = Unit
			})
		}
	}

	private fun isDebuggable(): Boolean {
		return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
	}
}
