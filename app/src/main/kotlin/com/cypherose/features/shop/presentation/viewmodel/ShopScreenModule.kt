package com.cypherose.features.shop.presentation.viewmodel

import com.cypherose.features.shop.presentation.state.ShopScreenReducer
import com.cypherose.features.shop.presentation.state.ShopScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ShopScreenModule {
	@Provides
	fun provideShopScreenReducer(): ShopScreenReducer {
		return ShopScreenReducerImpl()
	}
}
