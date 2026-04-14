package com.app.feature.login.ui

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)
