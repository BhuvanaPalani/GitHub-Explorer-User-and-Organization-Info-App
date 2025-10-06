package se.linerotech.module202.project2.home

import se.linerotech.module202.project2.api.UserDto

sealed class HomeUiState {
    data object Idle : HomeUiState()
    data object Loading : HomeUiState()
    data class UserExists(val user: UserDto) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
