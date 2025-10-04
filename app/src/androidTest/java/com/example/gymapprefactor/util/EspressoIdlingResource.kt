package com.example.gymapprefactor.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
	private val countingIdlingResource = CountingIdlingResource("GLOBAL")
	fun increment() = countingIdlingResource.increment()
	fun decrement() = countingIdlingResource.decrement()
	fun get() = countingIdlingResource
}