package com.cypherose.di

import dagger.Module
import dagger.Provides
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.util.EspressoIdlingResource
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object TestDispatchersModule {

	@Provides
	@Singleton
	fun provideDispatcherProvider(): DispatcherProvider = IntegrationTestDispatcherProvider()
}

class IntegrationTestDispatcherProvider : DispatcherProvider {
	override val main: CoroutineDispatcher = Dispatchers.Main
	override val io: CoroutineDispatcher = CountingDispatcher(Dispatchers.IO)
	override val default: CoroutineDispatcher = CountingDispatcher(Dispatchers.Default)
	override val unconfined: CoroutineDispatcher = CountingDispatcher(Dispatchers.Unconfined)
}

class CountingDispatcher(private val delegate: CoroutineDispatcher) :
	CoroutineDispatcher() {
	override fun dispatch(context: CoroutineContext, block: Runnable) {
		EspressoIdlingResource.increment()
		delegate.dispatch(context) {
			try {
				block.run()
			} finally {
				EspressoIdlingResource.decrement()
			}
		}
	}

	override fun isDispatchNeeded(context: CoroutineContext): Boolean = delegate.isDispatchNeeded(context)
}