package com.app.partyzone.ui.screen.setting

sealed interface SettingEffect {
    data object NavigateToUpdateProfile : SettingEffect
    data object NavigateToLogin : SettingEffect
    data class ShowToast(val message: String?) : SettingEffect
}