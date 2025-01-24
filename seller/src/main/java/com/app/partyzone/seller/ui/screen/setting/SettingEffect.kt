package com.app.partyzone.seller.ui.screen.setting

sealed interface SettingEffect {
    data object NavigateToUpdateProfile : SettingEffect
    data object NavigateToLogin : SettingEffect
    data class ShowToast(val message: String?) : SettingEffect
}