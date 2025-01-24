package com.app.partyzone.ui.screen.profile.update

import android.net.Uri
import com.app.partyzone.core.domain.entity.UpdateUser
import com.app.partyzone.core.domain.repository.UserRepository
import com.app.partyzone.ui.base.BaseViewModel
import com.app.partyzone.ui.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<UpdateProfileState, UpdateProfileEffect>(UpdateProfileState()) {

    init {
        initUser()
    }

    fun onClickIconBack() {
        sendNewEffect(UpdateProfileEffect.NavigateBack)
    }

    fun onNameChanged(name: String) {
        updateState { it.copy(name = name) }
    }

    fun onEmailChanged(email: String) {
        updateState { it.copy(email = email) }
    }

    fun onOldPasswordChanged(oldPassword: String) {
        updateState { it.copy(oldPassword = oldPassword) }
    }

    fun onNewPasswordChanged(newPassword: String) {
        updateState { it.copy(newPassword = newPassword) }
    }

    fun onImageUriChanged(imageUri: Uri?) {
        updateState { it.copy(imageUri = imageUri) }
    }

    private fun initUser() {
        updateState { it.copy(isLoadingGetUser = true) }
        tryToExecute(
            function = { userRepository.getCurrentUser() },
            onSuccess = {
                updateState {
                    it.copy(
                        isLoadingGetUser = false,
                        name = it.name,
                        email = it.email,
                        photoUrl = it.photoUrl
                    )
                }
            },
            onError = ::handleError
        )
    }

    fun onClickedUpdate() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                userRepository.updateCurrentUser(
                    UpdateUser(
                        name = state.value.name,
                        email = state.value.email,
                        oldPassword = state.value.oldPassword,
                        newPassword = state.value.newPassword,
                        imageUri = state.value.imageUri
                    )
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false) }
                sendNewEffect(UpdateProfileEffect.NavigateBack)
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
                        error = "Something happen please try again later!"
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast("No internet connection"))
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast(error.message.toString()))
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = "Something happen please try again later!"
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast(error.message.toString()))
            }

            is ErrorState.UnAuthorized -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast("Please login again"))
            }

            is ErrorState.InvalidPassword -> {
                updateState {
                    it.copy(
                        isLoading = false,
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast("Invalid password"))
            }

            else -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = "Something happen please try again later!"
                    )
                }
                sendNewEffect(UpdateProfileEffect.ShowToast("Something happen please try again later!"))
            }
        }
    }

    fun onClickRetry() {
        initUser()
    }
}