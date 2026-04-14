package com.app.data.repository

import com.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override suspend fun login(email: String, password: String): Boolean {
        return emailRegex.matches(email) && password.isNotBlank()
    }
}
