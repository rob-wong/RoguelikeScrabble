package com.cypherose.business.user.domain

import com.cypherose.business.interfaces.Repository
import com.cypherose.business.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : Repository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
    fun getUserFlow(): Flow<User?>
}
