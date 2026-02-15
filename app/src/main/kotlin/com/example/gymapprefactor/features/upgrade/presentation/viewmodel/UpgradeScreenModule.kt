package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import com.example.gymapprefactor.features.upgrade.domain.UpgradeCostMapper
import com.example.gymapprefactor.features.upgrade.domain.UpgradeCostMapperImpl
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeLetterStateMapper
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeLetterStateMapperImpl
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducer
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UpgradeScreenModule {
	@Provides
	fun provideUpgradeScreenReducer(): UpgradeScreenReducer {
		return UpgradeScreenReducerImpl()
	}

	@Provides
	fun provideUpgradeLetterStateMapper(): UpgradeLetterStateMapper {
		return UpgradeLetterStateMapperImpl()
	}

	@Provides
	fun provideUpgradeCostMapper(): UpgradeCostMapper {
		return UpgradeCostMapperImpl()
	}
}
