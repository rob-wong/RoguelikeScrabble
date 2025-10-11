package com.example.gymapprefactor.business.user.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.User

interface UserRepository : Repository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
}
