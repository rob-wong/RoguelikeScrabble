package com.cypherose.business.user.domain

import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.User

class SaveUserUseCase(override val repository: UserRepository) : UseCase {
	suspend operator fun invoke(user: User): Result<Unit> {
		return (repository as? com.cypherose.business.user.data.UserRepositoryImpl)?.let { repo ->
			repo.saveUserWithResult(user)
		} ?: run {
			repository.saveUser(user)
			Result.success(Unit)
		}
	}
}
