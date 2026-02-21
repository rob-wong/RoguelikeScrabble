package com.cypherose.business.user.domain

import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.User

class GetUserUseCase(override val repository: UserRepository) : UseCase {
    suspend operator fun invoke(): Result<User> {
        val user = repository.getUser()

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(IllegalStateException("User not found in repository"))
        }
    }
}
