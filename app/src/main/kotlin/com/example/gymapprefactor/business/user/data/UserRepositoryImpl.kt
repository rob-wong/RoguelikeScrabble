package com.example.gymapprefactor.business.user.data

import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.User
import com.example.gymapprefactor.business.user.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl(
    override val dataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser(): User? {
        return dataSource.fetchUser()
    }

    override suspend fun saveUser(user: User) {
        dataSource.saveUser(user)
    }
}

class UserDataSource @Inject constructor(
    private val appDataModel: AppDataModel
) : DataSource {
    fun fetchUser(): User? {
        return appDataModel.getCurrentUser()
    }
    
    suspend fun saveUser(user: User) {
        appDataModel.saveUser(user)
    }
}
