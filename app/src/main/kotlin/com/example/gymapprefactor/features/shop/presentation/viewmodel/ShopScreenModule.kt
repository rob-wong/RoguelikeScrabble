package com.example.gymapprefactor.features.shop.presentation.viewmodel

import com.example.gymapprefactor.features.shop.presentation.state.ShopScreenReducer
import com.example.gymapprefactor.features.shop.presentation.state.ShopScreenReducerImpl
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
