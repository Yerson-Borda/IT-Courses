package com.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val cyrillicRegex = Regex("[А-Яа-яЁё]")

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        val sanitized = value.replace(cyrillicRegex, "")
        _uiState.update { state ->
            state.copy(
                email = sanitized,
                isLoginEnabled = isLoginFormValid(sanitized, state.password)
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                isLoginEnabled = isLoginFormValid(state.email, value)
            )
        }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (!state.isLoginEnabled || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { current -> current.copy(isLoading = true) }
            val isLoggedIn = loginUseCase(state.email, state.password)
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }

    fun consumeLoginNavigation() {
        _uiState.update { state -> state.copy(isLoggedIn = false) }
    }

    private fun isLoginFormValid(email: String, password: String): Boolean {
        return emailRegex.matches(email) && password.isNotBlank()
    }

    class Factory @Inject constructor(
        private val loginUseCase: LoginUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(loginUseCase = loginUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
