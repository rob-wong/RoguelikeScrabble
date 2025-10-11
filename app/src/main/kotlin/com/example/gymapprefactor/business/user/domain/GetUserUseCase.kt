package com.example.gymapprefactor.business.user.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.User

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
