package com.app.partyzone.ui.screen.setting

import com.app.partyzone.core.domain.repository.AuthRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<SettingState, SettingEffect>(SettingState()) {

    fun onClickedEditProfile() {
        sendNewEffect(SettingEffect.NavigateToEditProfile)
    }

    fun onClickedLogout() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = authRepository::logout,
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                sendNewEffect(SettingEffect.NavigateToLogin)
            },
            onError = ::handleError
        )
    }

    private fun handleError(error: ErrorState) {
        when (error) {
            is ErrorState.NetworkError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(SettingEffect.ShowToast("No internet connection"))
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(SettingEffect.ShowToast(error.message.toString()))
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(SettingEffect.ShowToast(error.message.toString()))
            }

            else -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(SettingEffect.ShowToast("Something happen please try again later!"))
            }
        }
    }
}