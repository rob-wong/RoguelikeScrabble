package com.example.gymapprefactor.business.user.data

import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.User
import com.example.gymapprefactor.business.user.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl(
    override val dataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser(): User? {
        return dataSource.fetchUser()
    }

    override suspend fun saveUser(user: User) {
        dataSource.saveUser(user).getOrThrow()
    }

    suspend fun saveUserWithResult(user: User): Result<Unit> {
        return dataSource.saveUser(user)
    }

    override fun getUserFlow(): Flow<User?> {
        return dataSource.getUserFlow()
    }
}

class UserDataSource @Inject constructor(
    private val appDataModel: AppDataModel
) : DataSource {
    fun fetchUser(): User? {
        return appDataModel.getCurrentUser()
    }
    
    suspend fun saveUser(user: User): Result<Unit> {
        return appDataModel.saveUser(user).fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { Result.failure(it) }
        )
    }

    fun getUserFlow(): Flow<User?> {
        return appDataModel.userFlow
    }
}
