package com.example.gymapprefactor.business.user.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.User

class SaveUserUseCase(override val repository: UserRepository) : UseCase {
	suspend operator fun invoke(user: User): Result<Unit> {
		return (repository as? com.example.gymapprefactor.business.user.data.UserRepositoryImpl)?.let { repo ->
			repo.saveUserWithResult(user)
		} ?: run {
			repository.saveUser(user)
			Result.success(Unit)
		}
	}
}
