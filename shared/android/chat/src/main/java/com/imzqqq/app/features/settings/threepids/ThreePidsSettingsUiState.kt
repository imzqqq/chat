package com.imzqqq.app.features.settings.threepids

sealed class ThreePidsSettingsUiState {
    object Idle : ThreePidsSettingsUiState()
    data class AddingEmail(val error: String?) : ThreePidsSettingsUiState()
    data class AddingPhoneNumber(val error: String?) : ThreePidsSettingsUiState()
}
